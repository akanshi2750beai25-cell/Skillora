package com.skillora.model;

import java.sql.Timestamp;

/**
 * Model class representing an internship application submitted by a student.
 * Encapsulates application metadata, review status, and joined opportunity fields.
 * Demonstrates: Encapsulation, JavaBeans conventions.
 * 
 * Contributed by: Navdeep Randhawa
 */
public class Application {
    private int applicationId;
    private int studentId;
    private int internshipId;
    private Timestamp applicationDate;
    private String status;
    private String remarks;

    // Joined fields for display
    private String internshipTitle;
    private String companyName;

    public Application() {}

    public Application(int studentId, int internshipId, String status) {
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.status = status;
    }

    public Application(int applicationId, int studentId, int internshipId,
                       Timestamp applicationDate, String status, String remarks) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.internshipId = internshipId;
        this.applicationDate = applicationDate;
        this.status = status;
        this.remarks = remarks;
    }

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getInternshipId() { return internshipId; }
    public void setInternshipId(int internshipId) { this.internshipId = internshipId; }

    public Timestamp getApplicationDate() { return applicationDate; }
    public void setApplicationDate(Timestamp applicationDate) { this.applicationDate = applicationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public String getInternshipTitle() { return internshipTitle; }
    public void setInternshipTitle(String internshipTitle) { this.internshipTitle = internshipTitle; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
