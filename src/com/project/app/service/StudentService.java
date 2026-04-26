package com.project.app.service;

import java.sql.Connection;
import java.util.List;

import com.project.app.dao.*;
import com.project.app.exception.*;
import com.project.app.model.*;
import com.project.app.util.*;

public class StudentService {
	
	private static final int MAX_AGE = 100;

    private StudentDAO studentDAO = new StudentDAOImplementation();
    private RegistrationDAO registrationDAO = new RegistrationDAOImplementation();

    // 1. Add Student
    public void addStudent(Student s) throws ApplicationException {

        Connection con = DBUtil.getConnection();

        try {
            TransactionManager.begin(con);

            if (s.getStudentId() <= 0)
                throw new InvalidInputException("Student ID must be positive");

            if (s.getName() == null || s.getName().trim().isEmpty())
                throw new InvalidInputException("Name cannot be empty");

            if (s.getAge() <= 0 || s.getAge() > MAX_AGE)
                throw new InvalidInputException("Age must be between 1 and " + MAX_AGE);

            if (studentDAO.exists(con, s.getStudentId()))
                throw new DuplicateStudentException("Student already exists");

            studentDAO.add(con, s);

            TransactionManager.commit(con);

        } catch (Exception e) {
            TransactionManager.rollback(con);

            if (e instanceof ApplicationException)
                throw (ApplicationException) e;

            throw new ApplicationException("Add student failed");

        } finally {
            TransactionManager.end(con);
        }
    }

    // 2. Register Course
    public void registerCourse(int studentId, String course, double fee) throws ApplicationException {

        Connection con = DBUtil.getConnection();

        try {
            TransactionManager.begin(con);

            if (studentId <= 0)
                throw new InvalidInputException("Invalid student ID");

            if (course == null || course.trim().isEmpty())
                throw new InvalidInputException("Course cannot be empty");

            if (fee <= 0)
                throw new InvalidInputException("Fee must be > 0");

            if (!studentDAO.exists(con, studentId))
                throw new StudentNotFoundException("Student not found");

            if (registrationDAO.isDuplicate(con, studentId, course))
                throw new RegistrationException("Duplicate course registration");

            registrationDAO.register(con, studentId, course, fee);

            TransactionManager.commit(con);

        } catch (Exception e) {
            TransactionManager.rollback(con);

            if (e instanceof ApplicationException)
                throw (ApplicationException) e;

            throw new ApplicationException("Course registration failed");

        } finally {
            TransactionManager.end(con);
        }
    }

    // 3. View All
    public void viewAll() throws ApplicationException {

        Connection con = DBUtil.getConnection();

        try {
            List<String> list = registrationDAO.getAllWithStudents(con);

            if (list.isEmpty()) {
                System.out.println("No records found");
            } else {
                list.forEach(System.out::println);
            }

        } catch (Exception e) {
            if (e instanceof ApplicationException)
                throw (ApplicationException) e;

            throw new ApplicationException("Fetch failed");

        } finally {
            TransactionManager.end(con);
        }
    }

    // 4. Search Student
    public void searchStudent(int studentId) throws ApplicationException {

        Connection con = DBUtil.getConnection();

        try {
            if (studentId <= 0)
                throw new InvalidInputException("Invalid student ID");

            if (!studentDAO.exists(con, studentId))
                throw new StudentNotFoundException("Student not found");

            Student s = studentDAO.getById(con, studentId);
            List<Registration> list = registrationDAO.getByStudentId(con, studentId);

            System.out.println("\nStudent: " + s.getName());

            if (list.isEmpty()) {
                System.out.println("No registrations");
            } else {
                list.forEach(r ->
                System.out.println(
                	    "Course: " + r.getCourseName() +
                	    " | Fee: " + r.getFeesPaid()
                	);
                        
            }

        } catch (Exception e) {
            if (e instanceof ApplicationException)
                throw (ApplicationException) e;

            throw new ApplicationException("Search failed");

        } finally {
            TransactionManager.end(con);
        }
    }

    // 5. Update Student
    public void updateStudent(int studentId, String name, String branch) throws ApplicationException {

        Connection con = DBUtil.getConnection();

        try {
            if (studentId <= 0)
                throw new InvalidInputException("Invalid student ID");

            if (name == null || name.trim().isEmpty())
                throw new InvalidInputException("Name cannot be empty");

            if (branch == null || branch.trim().isEmpty())
                throw new InvalidInputException("Branch cannot be empty");

            int rows = studentDAO.update(con, studentId, name, branch);

            if (rows == 0)
                throw new StudentNotFoundException("No matching record found");

        } catch (Exception e) {
            if (e instanceof ApplicationException)
                throw (ApplicationException) e;

            throw new ApplicationException("Update failed");

        } finally {
            TransactionManager.end(con);
        }
    }

    // 6. Update Fee
    public void updateFee(int studentId, String course, double fee) throws ApplicationException {

        Connection con = DBUtil.getConnection();

        try {

            if (!studentDAO.exists(con, studentId))
                throw new StudentNotFoundException("Student not found");
            
            if (fee <= 0)
                throw new InvalidInputException("Fee must be > 0");
            

            int rows = registrationDAO.updateFee(con, studentId, course, fee);

            if (rows == 0)
                throw new RegistrationException("No matching registration");

        } catch (Exception e) {
            if (e instanceof ApplicationException)
                throw (ApplicationException) e;

            throw new ApplicationException("Fee update failed");

        } finally {
            TransactionManager.end(con);
        }
    }

    // 7. Cancel Registration
    public void cancelRegistration(int studentId, String course) throws ApplicationException {

        Connection con = DBUtil.getConnection();

        try {
        	
        	if (course == null || course.trim().isEmpty()) {
        	    throw new InvalidInputException("Course cannot be empty");
        	}
        	
            if (!studentDAO.exists(con, studentId))
                throw new StudentNotFoundException("Student not found");

            int rows = registrationDAO.deleteRegistration(con, studentId, course);

            if (rows == 0)
                throw new RegistrationException("No registration found");

        } catch (Exception e) {
            if (e instanceof ApplicationException)
                throw (ApplicationException) e;

            throw new ApplicationException("Cancellation failed");

        } finally {
            TransactionManager.end(con);
        }
    }

    // 8. Delete Student
    public void deleteStudent(int studentId) throws ApplicationException {

        Connection con = DBUtil.getConnection();

        try {
            TransactionManager.begin(con);

            if (!studentDAO.exists(con, studentId))
                throw new StudentNotFoundException("Student not found");

            registrationDAO.deleteByStudentId(con, studentId);

            int rows = studentDAO.delete(con, studentId);

            if (rows == 0)
                throw new ApplicationException("No matching record found");

            TransactionManager.commit(con);

        } catch (Exception e) {
            TransactionManager.rollback(con);

            if (e instanceof ApplicationException)
                throw (ApplicationException) e;

            throw new ApplicationException("Delete failed");

        } finally {
            TransactionManager.end(con);
        }
    }

    // 9. High Paying
    public void highPayingStudents(double fee) throws ApplicationException {

        Connection con = DBUtil.getConnection();

        try {
            List<Registration> list = registrationDAO.getHighPaying(con, fee);

            if (list.isEmpty()) {
                System.out.println("No high paying students");
            } else {
                list.forEach(r ->
                        System.out.println(r.getStudentId() + " | " + r.getCourseName() + " | " + r.getFeesPaid()));
            }

        } catch (Exception e) {
            if (e instanceof ApplicationException)
                throw (ApplicationException) e;

            throw new ApplicationException("Fetch failed");

        } finally {
            TransactionManager.end(con);
        }
    }

    // 10. Course Count
    public void courseWiseCount() throws ApplicationException {

        Connection con = DBUtil.getConnection();

        try {
            List<String> list = registrationDAO.getCourseWiseCount(con);

            if (list.isEmpty()) {
                System.out.println("No data found");
            } else {
                list.forEach(System.out::println);
            }

        } catch (Exception e) {
            if (e instanceof ApplicationException)
                throw (ApplicationException) e;

            throw new ApplicationException("Fetch failed");

        } finally {
            TransactionManager.end(con);
        }
    }
}