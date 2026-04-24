package com.project.app.model;

public class Registration {

    private int studentId;
    private String courseName;
    private double feesPaid;

    public Registration(int studentId, String courseName, double feesPaid) {
        this.studentId = studentId;
        this.courseName = courseName;
        this.feesPaid = feesPaid;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getCourseName() {
        return courseName;
    }

    public double getFeesPaid() {
        return feesPaid;
    }
}