package com.skillora.service;

import com.skillora.dao.StudentDAO;
import com.skillora.dao.StudentDAOImpl;
import com.skillora.exception.AuthenticationException;
import com.skillora.exception.SkilloraException;
import com.skillora.model.Student;
import com.skillora.model.User;
import com.skillora.utility.InputValidator;

import java.sql.SQLException;

/**
 * Service managing user authentication, registration, input validation, and user session state.
 * 
 * Contributed by: Akanshi
 */
public class AuthService {

    private final StudentDAO studentDAO;
    private User currentUser;

    public AuthService() {
        this.studentDAO = new StudentDAOImpl();
    }

    public AuthService(StudentDAO studentDAO) {
        this.studentDAO = studentDAO;
    }

    public User login(String email, String password) throws SkilloraException {
        if (!InputValidator.isValidEmail(email)) {
            throw new AuthenticationException("Please enter a valid email address.");
        }
        if (!InputValidator.isNonEmpty(password)) {
            throw new AuthenticationException("Password cannot be empty.");
        }

        try {
            User user = studentDAO.authenticate(email.trim(), password);
            if (user == null) {
                throw new AuthenticationException("Invalid email or password. Please try again.");
            }
            this.currentUser = user;
            return user;
        } catch (SQLException e) {
            throw new SkilloraException("Database error during authentication: " + e.getMessage(), e);
        }
    }

    public boolean register(Student student) throws SkilloraException {
        if (!InputValidator.isValidEmail(student.getEmail())) {
            throw new SkilloraException("Invalid email format. E.g. name@domain.com");
        }
        if (!InputValidator.isValidPassword(student.getPassword())) {
            throw new SkilloraException("Password must be at least 4 characters.");
        }
        if (!InputValidator.isNonEmpty(student.getName())) {
            throw new SkilloraException("Full name is required.");
        }
        if (!InputValidator.isNonEmpty(student.getRollNumber())) {
            throw new SkilloraException("Roll number is required.");
        }
        if (!InputValidator.isValidCgpa(student.getCgpa())) {
            throw new SkilloraException("CGPA must be between 0.00 and 10.00.");
        }
        if (!InputValidator.isValidSemester(student.getSemester())) {
            throw new SkilloraException("Semester must be between 1 and 12.");
        }

        try {
            return studentDAO.registerStudent(student);
        } catch (SQLException e) {
            if (e.getMessage().toLowerCase().contains("duplicate") || e.getMessage().contains("UNIQUE")) {
                throw new SkilloraException("An account with this email or roll number already exists.");
            }
            throw new SkilloraException("Registration failed: " + e.getMessage(), e);
        }
    }

    public boolean updateProfile(Student updatedStudent) throws SkilloraException {
        try {
            boolean success = studentDAO.updateStudentProfile(updatedStudent);
            if (success && currentUser instanceof Student) {
                this.currentUser = studentDAO.getStudentProfile(currentUser.getUserId());
            }
            return success;
        } catch (SQLException e) {
            throw new SkilloraException("Profile update failed: " + e.getMessage(), e);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
