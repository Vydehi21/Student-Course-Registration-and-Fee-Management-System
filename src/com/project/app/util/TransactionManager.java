package com.project.app.util;

import java.sql.Connection;

public class TransactionManager {

    public static void begin(Connection con) throws Exception {
    	if (con != null) {
            con.setAutoCommit(false);
        }
    }

    public static void commit(Connection con) throws Exception {
    	if (con != null) {
            con.commit();
        }
    }

    public static void rollback(Connection con) {
        try {
            if (con != null) con.rollback();
        } catch (Exception e) {
            System.out.println("Rollback error:"+  e.getMessage());
        }
    }

    public static void end(Connection con) {
        try {
            if (con != null) {
            	con.setAutoCommit(true);
            	con.close(); 
            }
        } catch (Exception e) {
            System.out.println("Connection close failed: " + e.getMessage());
        }
    }
}