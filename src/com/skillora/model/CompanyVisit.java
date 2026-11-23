package com.skillora.model;

import java.sql.Date;

/**
 * Represents an upcoming campus recruitment visit by a company.
 * Demonstrates: Encapsulation, Constructors, Java Date handling.
 */
public class CompanyVisit {

    private int visitId;
    private int companyId;
    private String companyName; // Populated from join for easy display
    private String collegeName;
    private Date visitDate;
    private String driveType; // "On-Campus", "Pool-Campus", "Virtual"
    private String eligibleBranches;

    public CompanyVisit() {}

    public CompanyVisit(int companyId, String collegeName, Date visitDate, String driveType, String eligibleBranches) {
        this.companyId = companyId;
        this.collegeName = collegeName;
        this.visitDate = visitDate;
        this.driveType = driveType;
        this.eligibleBranches = eligibleBranches;
    }

    public CompanyVisit(int visitId, int companyId, String companyName, String collegeName,
                        Date visitDate, String driveType, String eligibleBranches) {
        this.visitId = visitId;
        this.companyId = companyId;
        this.companyName = companyName;
        this.collegeName = collegeName;
        this.visitDate = visitDate;
        this.driveType = driveType;
        this.eligibleBranches = eligibleBranches;
    }

    public int getVisitId() {
        return visitId;
    }

    public void setVisitId(int visitId) {
        this.visitId = visitId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getEligibleBranches() {
        return eligibleBranches;
    }

    public void setEligibleBranches(String eligibleBranches) {
        this.eligibleBranches = eligibleBranches;
    }
}
