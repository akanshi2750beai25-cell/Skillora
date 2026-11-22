package com.skillora.dao;

import com.skillora.model.Student;
import com.skillora.model.User;
import java.sql.SQLException;

/**
 * Data Access Object interface for User and Student entities.
 * Defines contract for registration, authentication, and profile management.
 * 
 * Contributed by: Akanshi
 */
public interface StudentDAO {
    boolean registerStudent(Student student) throws SQLException;
    User authenticate(String email, String password) throws SQLException;
    Student getStudentProfile(int studentId) throws SQLException;
    boolean updateStudentProfile(Student student) throws SQLException;
}
