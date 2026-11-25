package com.skillora.service;

import com.skillora.dao.InternshipDAO;
import com.skillora.dao.InternshipDAOImpl;
import com.skillora.exception.DuplicateApplicationException;
import com.skillora.exception.IneligibleStudentException;
import com.skillora.exception.SkilloraException;
import com.skillora.model.Application;
import com.skillora.model.Internship;
import com.skillora.model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class implementing business logic and validation for internship opportunities and applications.
 * Enforces academic eligibility rules and prevents duplicate applications.
 * Demonstrates: Business logic layer, custom exception throwing, and DAO orchestration.
 * 
 * Contributed by: Navdeep Randhawa
 */
public class InternshipService {

    private final InternshipDAO internshipDAO;

    public InternshipService() {
        this.internshipDAO = new InternshipDAOImpl();
    }

    public InternshipService(InternshipDAO internshipDAO) {
        this.internshipDAO = internshipDAO;
    }

    public List<Internship> getAllOpenInternships() throws SkilloraException {
        try {
            return internshipDAO.getAllInternships();
        } catch (SQLException e) {
            throw new SkilloraException("Failed to fetch internships: " + e.getMessage(), e);
        }
    }

    public Internship getInternship(int id) throws SkilloraException {
        try {
            return internshipDAO.getInternshipById(id);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to find internship #" + id + ": " + e.getMessage(), e);
        }
    }

    public List<Internship> getEligibleInternships(Student student) throws SkilloraException {
        List<Internship> all = getAllOpenInternships();
        List<Internship> eligible = new ArrayList<>();

        for (Internship i : all) {
            if (isStudentEligible(student, i)) {
                eligible.add(i);
            }
        }
        return eligible;
    }

    public boolean isStudentEligible(Student student, Internship internship) {
        if (student == null || internship == null) return false;

        // Check CGPA cutoff
        if (student.getCgpa() < internship.getMinCgpa()) {
            return false;
        }

        // Check semester requirement
        if (student.getSemester() < internship.getMinSemester()) {
            return false;
        }

        // Check branch eligibility
        if (internship.getEligibleBranches() != null && !internship.getEligibleBranches().trim().isEmpty()) {
            String allowed = internship.getEligibleBranches().toLowerCase();
            String studentBranch = student.getBranch() != null ? student.getBranch().toLowerCase() : "";
            if (!allowed.contains(studentBranch)) {
                return false;
            }
        }

        return true;
    }

    public boolean apply(Student student, int internshipId) throws SkilloraException {
        try {
            Internship internship = internshipDAO.getInternshipById(internshipId);
            if (internship == null) {
                throw new SkilloraException("Internship opening #" + internshipId + " does not exist.");
            }

            // Check if already applied (Business constraint)
            if (internshipDAO.hasApplied(student.getUserId(), internshipId)) {
                throw new DuplicateApplicationException("You have already submitted an application for " + internship.getTitle() + " at " + internship.getCompanyName() + ".");
            }

            // Check academic eligibility
            if (!isStudentEligible(student, internship)) {
                String reason = String.format("You do not meet the eligibility requirements (Min CGPA: %.2f, Min Sem: %d, Allowed Branches: %s).",
                        internship.getMinCgpa(), internship.getMinSemester(), internship.getEligibleBranches());
                throw new IneligibleStudentException(reason);
            }

            Application application = new Application(student.getUserId(), internshipId, "APPLIED");
            return internshipDAO.applyForInternship(application);

        } catch (SQLException e) {
            throw new SkilloraException("Application submission failed: " + e.getMessage(), e);
        }
    }

    public List<Application> getStudentApplications(int studentId) throws SkilloraException {
        try {
            return internshipDAO.getApplicationsByStudentId(studentId);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to fetch applications: " + e.getMessage(), e);
        }
    }

    public List<Application> getAllApplications() throws SkilloraException {
        try {
            return internshipDAO.getAllApplications();
        } catch (SQLException e) {
            throw new SkilloraException("Failed to fetch all applications: " + e.getMessage(), e);
        }
    }

    public boolean createInternship(Internship internship) throws SkilloraException {
        try {
            return internshipDAO.addInternship(internship);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to post internship: " + e.getMessage(), e);
        }
    }
}
