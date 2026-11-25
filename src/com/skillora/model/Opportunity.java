package com.skillora.model;

import java.sql.Date;

/**
 * Abstract class representing career opportunities posted on Skillora.
 * Subclasses include Internship.
 * Demonstrates: Abstraction, Encapsulation, Polymorphism base contract.
 * 
 * Contributed by: Navdeep Randhawa
 */
public abstract class Opportunity {

    protected int id;
    protected String title;
    protected int companyId;
    protected String companyName; // Populated from join for convenience
    protected String description;
    protected Date deadline;
    protected String status; // "OPEN", "CLOSED"

    public Opportunity() {}

    public Opportunity(int id, String title, int companyId, String description, Date deadline, String status) {
        this.id = id;
        this.title = title;
        this.companyId = companyId;
        this.description = description;
        this.deadline = deadline;
        this.status = status;
    }

    // Abstract method to be overridden by subclasses (Polymorphism)
    public abstract void displayDetails();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
