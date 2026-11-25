package com.skillora.model;

import java.sql.Date;

/**
 * Represents an internship opportunity with comprehensive eligibility criteria.
 * Inherits core listing details from Opportunity.
 * Demonstrates: Inheritance, 'super' keyword, Polymorphism (Method Overriding), Encapsulation.
 * 
 * Contributed by: Navdeep Randhawa
 */
public class Internship extends Opportunity {

    private double stipend;
    private int durationMonths;
    private String location;
    private double minCgpa;
    private String eligibleBranches;
    private int minSemester;
    private String requiredSkills;

    public Internship() {
        super();
    }

    public Internship(int id, String title, int companyId, String description, Date deadline, String status,
                      double stipend, int durationMonths, String location, double minCgpa,
                      String eligibleBranches, int minSemester, String requiredSkills) {
        super(id, title, companyId, description, deadline, status);
        this.stipend = stipend;
        this.durationMonths = durationMonths;
        this.location = location;
        this.minCgpa = minCgpa;
        this.eligibleBranches = eligibleBranches;
        this.minSemester = minSemester;
        this.requiredSkills = requiredSkills;
    }

    // Implementing abstract method from Opportunity (Polymorphism & Method Overriding)
    @Override
    public void displayDetails() {
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("Internship ID: %d | Role: %s\n", id, title);
        System.out.printf("Company: %s (ID: %d) | Location: %s | Status: %s\n",
                companyName != null ? companyName : "Company #" + companyId, companyId, location, status);
        System.out.printf("Stipend: ₹%,.2f/month | Duration: %d Months | Deadline: %s\n", stipend, durationMonths, deadline);
        System.out.printf("Eligibility -> Min CGPA: %.2f | Min Semester: %d | Branches: %s\n", minCgpa, minSemester, eligibleBranches);
        System.out.printf("Required Skills: %s\n", requiredSkills != null ? requiredSkills : "Open");
        System.out.printf("Description: %s\n", description != null ? description : "N/A");
        System.out.println("--------------------------------------------------------------------------------");
    }

    public double getStipend() {
        return stipend;
    }

    public void setStipend(double stipend) {
        this.stipend = stipend;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getMinCgpa() {
        return minCgpa;
    }

    public void setMinCgpa(double minCgpa) {
        this.minCgpa = minCgpa;
    }

    public String getEligibleBranches() {
        return eligibleBranches;
    }

    public void setEligibleBranches(String eligibleBranches) {
        this.eligibleBranches = eligibleBranches;
    }

    public int getMinSemester() {
        return minSemester;
    }

    public void setMinSemester(int minSemester) {
        this.minSemester = minSemester;
    }

    public String getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(String requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}
