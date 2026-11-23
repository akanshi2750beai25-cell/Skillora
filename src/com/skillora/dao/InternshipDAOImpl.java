package com.skillora.dao;

import com.skillora.database.DBConnection;
import com.skillora.model.Application;
import com.skillora.model.Internship;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of InternshipDAO interface.
 * Handles database operations for internship listings, duplicate checking, and applications.
 * 
 * Contributed by: Navdeep Randhawa
 */
public class InternshipDAOImpl implements InternshipDAO {

    @Override
    public List<Internship> getAllInternships() throws SQLException {
        List<Internship> list = new ArrayList<>();
        String sql = "SELECT i.*, c.company_name FROM internships i " +
                     "JOIN companies c ON i.company_id = c.company_id " +
                     "WHERE i.status = 'OPEN' ORDER BY i.deadline ASC";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Internship intern = new Internship(
                        rs.getInt("internship_id"),
                        rs.getString("title"),
                        rs.getInt("company_id"),
                        rs.getString("description"),
                        rs.getDate("deadline"),
                        rs.getString("status"),
                        rs.getDouble("stipend"),
                        rs.getInt("duration_months"),
                        rs.getString("location"),
                        rs.getDouble("min_cgpa"),
                        rs.getString("eligible_branches"),
                        rs.getInt("min_semester"),
                        rs.getString("required_skills")
                );
                intern.setCompanyName(rs.getString("company_name"));
                list.add(intern);
            }
        }
        return list;
    }

    @Override
    public Internship getInternshipById(int internshipId) throws SQLException {
        String sql = "SELECT i.*, c.company_name FROM internships i " +
                     "JOIN companies c ON i.company_id = c.company_id " +
                     "WHERE i.internship_id = ?";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, internshipId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Internship intern = new Internship(
                            rs.getInt("internship_id"),
                            rs.getString("title"),
                            rs.getInt("company_id"),
                            rs.getString("description"),
                            rs.getDate("deadline"),
                            rs.getString("status"),
                            rs.getDouble("stipend"),
                            rs.getInt("duration_months"),
                            rs.getString("location"),
                            rs.getDouble("min_cgpa"),
                            rs.getString("eligible_branches"),
                            rs.getInt("min_semester"),
                            rs.getString("required_skills")
                    );
                    intern.setCompanyName(rs.getString("company_name"));
                    return intern;
                }
            }
        }
        return null;
    }

    @Override
    public boolean addInternship(Internship internship) throws SQLException {
        String sql = "INSERT INTO internships (company_id, title, description, stipend, duration_months, " +
                     "location, min_cgpa, eligible_branches, min_semester, required_skills, deadline, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'OPEN')";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, internship.getCompanyId());
            stmt.setString(2, internship.getTitle());
            stmt.setString(3, internship.getDescription());
            stmt.setDouble(4, internship.getStipend());
            stmt.setInt(5, internship.getDurationMonths());
            stmt.setString(6, internship.getLocation());
            stmt.setDouble(7, internship.getMinCgpa());
            stmt.setString(8, internship.getEligibleBranches());
            stmt.setInt(9, internship.getMinSemester());
            stmt.setString(10, internship.getRequiredSkills());
            stmt.setDate(11, internship.getDeadline());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean hasApplied(int studentId, int internshipId) throws SQLException {
        String sql = "SELECT 1 FROM applications WHERE student_id = ? AND internship_id = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, internshipId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean applyForInternship(Application application) throws SQLException {
        String sql = "INSERT INTO applications (student_id, internship_id, status) VALUES (?, ?, 'APPLIED')";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, application.getStudentId());
            stmt.setInt(2, application.getInternshipId());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Application> getApplicationsByStudentId(int studentId) throws SQLException {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, i.title, c.company_name " +
                     "FROM applications a " +
                     "JOIN internships i ON a.internship_id = i.internship_id " +
                     "JOIN companies c ON i.company_id = c.company_id " +
                     "WHERE a.student_id = ? ORDER BY a.application_date DESC";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Application app = new Application(
                            rs.getInt("application_id"),
                            rs.getInt("student_id"),
                            rs.getInt("internship_id"),
                            rs.getTimestamp("application_date"),
                            rs.getString("status"),
                            rs.getString("remarks")
                    );
                    app.setInternshipTitle(rs.getString("title"));
                    app.setCompanyName(rs.getString("company_name"));
                    list.add(app);
                }
            }
        }
        return list;
    }

    @Override
    public List<Application> getAllApplications() throws SQLException {
        List<Application> list = new ArrayList<>();
        String sql = "SELECT a.*, i.title, c.company_name " +
                     "FROM applications a " +
                     "JOIN internships i ON a.internship_id = i.internship_id " +
                     "JOIN companies c ON i.company_id = c.company_id " +
                     "ORDER BY a.application_date DESC";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Application app = new Application(
                        rs.getInt("application_id"),
                        rs.getInt("student_id"),
                        rs.getInt("internship_id"),
                        rs.getTimestamp("application_date"),
                        rs.getString("status"),
                        rs.getString("remarks")
                );
                app.setInternshipTitle(rs.getString("title"));
                app.setCompanyName(rs.getString("company_name"));
                list.add(app);
            }
        }
        return list;
    }
}
