package com.project.app.dao;

import java.sql.*;
import java.util.*;

import com.project.app.model.Student;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public int add(Connection con, Student s) throws SQLException {
        String sql = "INSERT INTO student(id, name, age, branch) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, s.getStudentId());
            ps.setString(2, s.getName());
            ps.setInt(3, s.getAge());
            ps.setString(4, s.getBranch());
            return ps.executeUpdate();
        }
    }

    @Override
    public int update(Connection con, int id, String name, String branch) throws SQLException {
        String sql = "UPDATE student SET name=?, branch=? WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, branch);
            ps.setInt(3, id);
            return ps.executeUpdate();
        }
    }

    @Override
    public int delete(Connection con, int id) throws SQLException {
        String sql = "DELETE FROM student WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    @Override
    public boolean exists(Connection con, int id) throws SQLException {
        String sql = "SELECT 1 FROM student WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Student getById(Connection con, int id) throws SQLException {
        String sql = "SELECT id, name, age, branch FROM student WHERE id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("branch")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Student> getAll(Connection con) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT id, name, age, branch FROM student";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("branch")
                ));
            }
        }
        return list;
    }
}