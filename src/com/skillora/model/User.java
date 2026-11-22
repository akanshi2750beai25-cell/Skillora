package com.skillora.model;

import java.sql.Timestamp;

/**
 * Abstract base class representing all users in the Skillora system.
 * Subclasses include Student and Admin.
 * Demonstrates: Abstraction, Encapsulation, Inheritance base, Constructors, 'this' keyword.
 * 
 * Contributed by: Akanshi
 */
public abstract class User {

    protected int userId;
    protected String name;
    protected String email;
    protected String password;
    protected String role; // "STUDENT" or "ADMIN"
    protected Timestamp createdAt;

    // Default Constructor
    public User() {}

    // Constructor for registration / creation
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Full Parameterized Constructor
    public User(int userId, String name, String email, String password, String role, Timestamp createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Abstract method to be overridden by subclasses (Polymorphism & Abstraction)
    public abstract String getDetails();

    // Getters and Setters (Encapsulation)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
