package com.skillora.model;

import java.sql.Timestamp;

/**
 * Represents a placement officer or college administrator.
 * Inherits common authentication properties from User.
 * Demonstrates: Inheritance, 'super' keyword, Polymorphism (method overriding).
 * 
 * Contributed by: Akanshi
 */
public class Admin extends User {

    private String department;
    private String staffId;

    public Admin() {
        super();
        this.role = "ADMIN";
    }

    public Admin(String name, String email, String password, String department, String staffId) {
        super(name, email, password, "ADMIN");
        this.department = department;
        this.staffId = staffId;
    }

    public Admin(int userId, String name, String email, String password, Timestamp createdAt,
                 String department, String staffId) {
        super(userId, name, email, password, "ADMIN", createdAt);
        this.department = department;
        this.staffId = staffId;
    }

    @Override
    public String getDetails() {
        return String.format("Placement Admin: %s | Staff ID: %s | Department: %s",
                name, staffId, department);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }
}
