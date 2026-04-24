package com.project.app.dao;

import java.sql.*;
import java.util.*;

import com.project.app.model.Registration;

public class RegistrationDAOImpl implements RegistrationDAO {

    @Override
    public int register(Connection con, int id, String course, double fee) throws SQLException {
        String sql = "INSERT INTO registration(student_id, course_name, fees_paid) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, course);
            ps.setDouble(3, fee);
            return ps.executeUpdate();
        }
    }

    @Override
    public int updateFee(Connection con, int id, String course, double fee) throws SQLException {
        String sql = "UPDATE registration SET fees_paid=? WHERE student_id=? AND course_name=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, fee);
            ps.setInt(2, id);
            ps.setString(3, course);
            return ps.executeUpdate();
        }
    }

    @Override
    public int deleteRegistration(Connection con, int id, String course) throws SQLException {
        String sql = "DELETE FROM registration WHERE student_id=? AND course_name=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, course);
            return ps.executeUpdate();
        }
    }

    @Override
    public int deleteByStudentId(Connection con, int studentId) throws SQLException {
        String sql = "DELETE FROM registration WHERE student_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            return ps.executeUpdate();
        }
    }

    @Override
    public boolean isDuplicate(Connection con, int id, String course) throws SQLException {
        String sql = "SELECT 1 FROM registration WHERE student_id=? AND course_name=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, course);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Registration> getByStudentId(Connection con, int id) throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT student_id, course_name, fees_paid FROM registration WHERE student_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Registration(
                            rs.getInt("student_id"),
                            rs.getString("course_name"),
                            rs.getDouble("fees_paid")
                    ));
                }
            }
        }
        return list;
    }

    @Override
    public List<Registration> getHighPaying(Connection con, double fee) throws SQLException {
        List<Registration> list = new ArrayList<>();
        String sql = "SELECT student_id, course_name, fees_paid FROM registration WHERE fees_paid > ? ORDER BY fees_paid DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, fee);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Registration(
                            rs.getInt("student_id"),
                            rs.getString("course_name"),
                            rs.getDouble("fees_paid")
                    ));
                }
            }
        }
        return list;
    }

    @Override
    public List<String> getCourseWiseCount(Connection con) throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT course_name, COUNT(*) AS total FROM registration GROUP BY course_name ORDER BY total DESC";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(rs.getString("course_name") + " -> " + rs.getInt("total"));
            }
        }
        return list;
    }

    @Override
    public List<String> getAllWithStudents(Connection con) throws SQLException {
        List<String> list = new ArrayList<>();

        String sql = "SELECT s.id, s.name, r.course_name, r.fees_paid " +
                     "FROM student s LEFT JOIN registration r ON s.id = r.student_id " +
                     "ORDER BY s.id";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                String course = rs.getString("course_name");

                if (course == null) {
                    list.add(
                            rs.getInt("id") + " | " +
                            rs.getString("name") + " | No Courses"
                    );
                } else {
                    list.add(
                            rs.getInt("id") + " | " +
                            rs.getString("name") + " | " +
                            course + " | " +
                            rs.getDouble("fees_paid")
                    );
                }
            }
        }
        return list;
    }
}