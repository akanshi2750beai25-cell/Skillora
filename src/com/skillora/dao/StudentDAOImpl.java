package com.skillora.dao;

import com.skillora.database.DBConnection;
import com.skillora.model.Admin;
import com.skillora.model.Student;
import com.skillora.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementation of StudentDAO for MySQL using JDBC.
 * Implements transaction management, prepared statements, and role-based entity instantiation.
 * 
 * Contributed by: Akanshi
 */
public class StudentDAOImpl implements StudentDAO {

    @Override
    public boolean registerStudent(Student student) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String userSql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, 'STUDENT')";
        String studentSql = "INSERT INTO students (student_id, roll_number, college, branch, semester, cgpa) VALUES (?, ?, ?, ?, ?, ?)";

        boolean autoCommit = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement uStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS)) {
                uStmt.setString(1, student.getName());
                uStmt.setString(2, student.getEmail());
                uStmt.setString(3, student.getPassword());
                uStmt.executeUpdate();

                try (ResultSet rs = uStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int userId = rs.getInt(1);
                        student.setUserId(userId);

                        try (PreparedStatement sStmt = conn.prepareStatement(studentSql)) {
                            sStmt.setInt(1, userId);
                            sStmt.setString(2, student.getRollNumber());
                            sStmt.setString(3, student.getCollege());
                            sStmt.setString(4, student.getBranch());
                            sStmt.setInt(5, student.getSemester());
                            sStmt.setDouble(6, student.getCgpa());
                            sStmt.executeUpdate();
                        }
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(autoCommit);
        }
    }

    @Override
    public User authenticate(String email, String password) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    String name = rs.getString("name");
                    String role = rs.getString("role");

                    if ("STUDENT".equalsIgnoreCase(role)) {
                        return getStudentProfile(userId);
                    } else {
                        Admin admin = new Admin();
                        admin.setUserId(userId);
                        admin.setName(name);
                        admin.setEmail(email);
                        admin.setRole("ADMIN");
                        String adminSql = "SELECT department, staff_id FROM admins WHERE admin_id = ?";
                        try (PreparedStatement aStmt = conn.prepareStatement(adminSql)) {
                            aStmt.setInt(1, userId);
                            try (ResultSet aRs = aStmt.executeQuery()) {
                                if (aRs.next()) {
                                    admin.setDepartment(aRs.getString("department"));
                                    admin.setStaffId(aRs.getString("staff_id"));
                                }
                            }
                        }
                        return admin;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Student getStudentProfile(int studentId) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sql = "SELECT u.user_id, u.name, u.email, u.created_at, " +
                     "s.roll_number, s.college, s.branch, s.semester, s.cgpa " +
                     "FROM users u JOIN students s ON u.user_id = s.student_id " +
                     "WHERE u.user_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            "",
                            rs.getTimestamp("created_at"),
                            rs.getString("roll_number"),
                            rs.getString("college"),
                            rs.getString("branch"),
                            rs.getInt("semester"),
                            rs.getDouble("cgpa")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public boolean updateStudentProfile(Student student) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String userSql = "UPDATE users SET name = ? WHERE user_id = ?";
        String studentSql = "UPDATE students SET college = ?, branch = ?, semester = ?, cgpa = ? WHERE student_id = ?";

        boolean autoCommit = conn.getAutoCommit();
        try {
            conn.setAutoCommit(false);

            try (PreparedStatement uStmt = conn.prepareStatement(userSql)) {
                uStmt.setString(1, student.getName());
                uStmt.setInt(2, student.getUserId());
                uStmt.executeUpdate();
            }

            try (PreparedStatement sStmt = conn.prepareStatement(studentSql)) {
                sStmt.setString(1, student.getCollege());
                sStmt.setString(2, student.getBranch());
                sStmt.setInt(3, student.getSemester());
                sStmt.setDouble(4, student.getCgpa());
                sStmt.setInt(5, student.getUserId());
                sStmt.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(autoCommit);
        }
    }
}
