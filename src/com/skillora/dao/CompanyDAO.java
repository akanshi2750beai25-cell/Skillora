package com.skillora.dao;

import com.skillora.model.Company;
import com.skillora.model.CompanyVisit;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Companies and Placement Visits.
 * Contributed by: Paras Jain
 */
public interface CompanyDAO {
    List<Company> getAllCompanies() throws SQLException;
    boolean addCompany(Company company) throws SQLException;
    List<CompanyVisit> getAllVisits() throws SQLException;
    boolean addCompanyVisit(CompanyVisit visit) throws SQLException;
}
