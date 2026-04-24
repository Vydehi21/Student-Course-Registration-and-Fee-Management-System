package com.project.app.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.project.app.model.Registration;

public interface RegistrationDAO {

    int register(Connection con, int studentId, String course, double fee) throws SQLException;

    int updateFee(Connection con, int studentId, String course, double fee) throws SQLException;

    int deleteRegistration(Connection con, int studentId, String course) throws SQLException;

    int deleteByStudentId(Connection con, int studentId) throws SQLException;
    
    boolean isDuplicate(Connection con, int studentId, String course) throws SQLException;

    List<Registration> getByStudentId(Connection con, int studentId) throws SQLException;

    List<Registration> getHighPaying(Connection con, double fee) throws SQLException;

    List<String> getCourseWiseCount(Connection con) throws SQLException;

    List<String> getAllWithStudents(Connection con) throws SQLException;
}