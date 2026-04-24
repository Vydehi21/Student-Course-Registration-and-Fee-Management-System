package com.project.app.model;

public class Student {
	
	private int studentId;
    private String name;
    private int age;
    private String branch;

    public Student(int studentId, String name, int age, String branch) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.branch = branch;
    }

    public int getStudentId() { return studentId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getBranch() { return branch; }

}
