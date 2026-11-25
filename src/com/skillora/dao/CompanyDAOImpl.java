package com.skillora.dao;

import com.skillora.database.DBConnection;
import com.skillora.model.Company;
import com.skillora.model.CompanyVisit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of CompanyDAO.
 * Contributed by: Paras Jain
 */
public class CompanyDAOImpl implements CompanyDAO {

    @Override
    public List<Company> getAllCompanies() throws SQLException {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT * FROM companies ORDER BY company_name";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Company(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getString("industry"),
                        rs.getString("website"),
                        rs.getString("contact_email")
                ));
            }
        }
        return list;
    }

    @Override
    public boolean addCompany(Company company) throws SQLException {
        String sql = "INSERT INTO companies (company_name, industry, website, contact_email) VALUES (?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, company.getCompanyName());
            stmt.setString(2, company.getIndustry());
            stmt.setString(3, company.getWebsite());
            stmt.setString(4, company.getContactEmail());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<CompanyVisit> getAllVisits() throws SQLException {
        List<CompanyVisit> list = new ArrayList<>();
        String sql = "SELECT v.visit_id, v.company_id, c.company_name, v.college_name, " +
                     "v.visit_date, v.drive_type, v.eligible_branches " +
                     "FROM company_visits v JOIN companies c ON v.company_id = c.company_id " +
                     "ORDER BY v.visit_date ASC";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new CompanyVisit(
                        rs.getInt("visit_id"),
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getString("college_name"),
                        rs.getDate("visit_date"),
                        rs.getString("drive_type"),
                        rs.getString("eligible_branches")
                ));
            }
        }
        return list;
    }

    @Override
    public boolean addCompanyVisit(CompanyVisit visit) throws SQLException {
        String sql = "INSERT INTO company_visits (company_id, college_name, visit_date, drive_type, eligible_branches) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, visit.getCompanyId());
            stmt.setString(2, visit.getCollegeName());
            stmt.setDate(3, visit.getVisitDate());
            stmt.setString(4, visit.getDriveType());
            stmt.setString(5, visit.getEligibleBranches());
            return stmt.executeUpdate() > 0;
        }
    }
}
