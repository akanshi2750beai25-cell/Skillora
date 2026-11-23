package com.skillora.dao;

import com.skillora.model.Application;
import com.skillora.model.Internship;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for internship opportunities and applications.
 * Defines contract for posting openings, fetching listings, and application submissions.
 * 
 * Contributed by: Navdeep Randhawa
 */
public interface InternshipDAO {
    List<Internship> getAllInternships() throws SQLException;
    Internship getInternshipById(int internshipId) throws SQLException;
    boolean addInternship(Internship internship) throws SQLException;
    boolean hasApplied(int studentId, int internshipId) throws SQLException;
    boolean applyForInternship(Application application) throws SQLException;
    List<Application> getApplicationsByStudentId(int studentId) throws SQLException;
    List<Application> getAllApplications() throws SQLException;
}
