package com.skillora.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student profile in Skillora. Inherits common properties from User.
 * Demonstrates: Inheritance, 'super' keyword, Polymorphism (method overriding), Encapsulation, Collections.
 */
public class Student extends User {

    private String rollNumber;
    private String college;
    private String branch;
    private int semester;
    private double cgpa;
    private List<Skill> skills;

    // Default Constructor
    public Student() {
        super();
        this.skills = new ArrayList<>();
        this.role = "STUDENT";
    }

    // Constructor for new student registration
    public Student(String name, String email, String password, String rollNumber,
                   String college, String branch, int semester, double cgpa) {
        super(name, email, password, "STUDENT");
        this.rollNumber = rollNumber;
        this.college = college;
        this.branch = branch;
        this.semester = semester;
        this.cgpa = cgpa;
        this.skills = new ArrayList<>();
    }

    // Full Parameterized Constructor (Used when querying from database)
    public Student(int userId, String name, String email, String password, Timestamp createdAt,
                   String rollNumber, String college, String branch, int semester, double cgpa) {
        super(userId, name, email, password, "STUDENT", createdAt);
        this.rollNumber = rollNumber;
        this.college = college;
        this.branch = branch;
        this.semester = semester;
        this.cgpa = cgpa;
        this.skills = new ArrayList<>();
    }

    // Implementing abstract method from User (Method Overriding & Polymorphism)
    @Override
    public String getDetails() {
        return String.format("Student: %s | Roll No: %s | College: %s | Branch: %s | Sem: %d | CGPA: %.2f",
                name, rollNumber, college, branch, semester, cgpa);
    }

    // Getters and Setters (Encapsulation)
    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill) {
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        this.skills.add(skill);
    }
}
