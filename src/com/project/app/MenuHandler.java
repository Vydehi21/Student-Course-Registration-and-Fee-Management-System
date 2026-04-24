package com.project.app;

import java.util.Scanner;

import com.project.app.exception.ApplicationException;
import com.project.app.model.Student;
import com.project.app.service.StudentService;

public class MenuHandler {

    private StudentService service = new StudentService();

    public void start() {

        try (Scanner sc = new Scanner(System.in)) {

            while (true) {

                try {
                    System.out.println("\n====== MENU ======");
                    System.out.println("1. Add Student");
                    System.out.println("2. Register Course");
                    System.out.println("3. View All Students with Courses");
                    System.out.println("4. Search Student by ID");
                    System.out.println("5. Update Student");
                    System.out.println("6. Update Course Fee");
                    System.out.println("7. Cancel Registration");
                    System.out.println("8. Delete Student");
                    System.out.println("9. High Paying Students");
                    System.out.println("10. Course-wise Count");
                    System.out.println("11. Exit");

                    int choice = readInt(sc, "Enter your choice (1-11): ");

                    switch (choice) {

                        case 1:
                            System.out.println("\n--- Add Student ---");
                            int studentId = readInt(sc, "Enter Student ID: ");
                            String name = readString(sc, "Enter Name: ");
                            int age = readInt(sc, "Enter Age: ");
                            String branch = readString(sc, "Enter Branch: ");

                            service.addStudent(new Student(studentId, name, age, branch));
                            System.out.println("Student added successfully");
                            break;

                        case 2:
                            System.out.println("\n--- Register Course ---");
                            int regId = readInt(sc, "Enter Student ID: ");
                            String course = readString(sc, "Enter Course Name: ");
                            double fee = readDouble(sc, "Enter Fee: ");

                            service.registerCourse(regId, course, fee);
                            System.out.println("Course registered successfully");
                            break;

                        case 3:
                            service.viewAll();
                            break;

                        case 4:
                            service.searchStudent(readInt(sc, "Enter Student ID: "));
                            break;

                        case 5:
                            int updateId = readInt(sc, "Enter Student ID: ");
                            String updatedName = readString(sc, "Enter New Name: ");
                            String updatedBranch = readString(sc, "Enter New Branch: ");

                            service.updateStudent(updateId, updatedName, updatedBranch);
                            System.out.println("Student updated successfully");
                            break;

                        case 6:
                            int feeId = readInt(sc, "Enter Student ID: ");
                            String feeCourse = readString(sc, "Enter Course Name: ");
                            double newFee = readDouble(sc, "Enter New Fee: ");

                            service.updateFee(feeId, feeCourse, newFee);
                            System.out.println("Fee updated successfully");
                            break;

                        case 7:
                            int cancelId = readInt(sc, "Enter Student ID: ");
                            String cancelCourse = readString(sc, "Enter Course Name: ");

                            service.cancelRegistration(cancelId, cancelCourse);
                            System.out.println("Registration cancelled");
                            break;

                        case 8:
                            int deleteId = readInt(sc, "Enter Student ID: ");

                            while (true) {
                                String confirm = readString(sc, "Are you sure? (yes/no): ");

                                if (confirm.equalsIgnoreCase("yes")) {
                                    service.deleteStudent(deleteId);
                                    System.out.println("Student deleted successfully");
                                    break;
                                } else if (confirm.equalsIgnoreCase("no")) {
                                    System.out.println("Deletion cancelled");
                                    break;
                                } else {
                                    System.out.println("Invalid input. Type yes/no.");
                                }
                            }
                            break;

                        case 9:
                            double minFee = readDouble(sc, "Enter minimum fee: ");
                            service.highPayingStudents(minFee);
                            break;

                        case 10:
                            service.courseWiseCount();
                            break;

                        case 11:
                            System.out.println("Exiting...");
                            return;

                        default:
                            System.out.println("Invalid choice!");
                    }

                } catch (ApplicationException e) {
                    System.out.println("Error: " + e.getMessage());

                } catch (Exception e) {
                    System.out.println("System error occurred. Try again.");
                }
            }
        }
    }

    private int readInt(Scanner sc, String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid number!");
            }
        }
    }

    private double readDouble(Scanner sc, String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid decimal!");
            }
        }
    }

    private String readString(Scanner sc, String msg) {
        while (true) {
            System.out.print(msg);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty!");
                continue;
            }

            if (input.matches("\\d+")) {
                System.out.println("Invalid input! Cannot be only numbers.");
                continue;
            }

            return input;
        }
    }
}