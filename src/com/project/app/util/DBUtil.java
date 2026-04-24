package com.project.app.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

	private static final String URL =
	        "jdbc:mysql://localhost:3306/student_registration";

    private static final String USER = "root";
    private static final String PASS = "Puppy2107@"; // change if needed

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            throw new RuntimeException("DB Connection failed: " + e.getMessage());
        }
    }
}