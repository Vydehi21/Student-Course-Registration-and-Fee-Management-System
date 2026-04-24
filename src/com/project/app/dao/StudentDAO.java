package com.project.app.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.project.app.model.Student;

public interface StudentDAO {

    int add(Connection con, Student s) throws SQLException;

    int update(Connection con, int studentId, String name, String branch) throws SQLException;

    int delete(Connection con, int studentId) throws SQLException;

    boolean exists(Connection con, int studentId) throws SQLException;

    Student getById(Connection con, int studentId) throws SQLException;

    List<Student> getAll(Connection con) throws SQLException;
}