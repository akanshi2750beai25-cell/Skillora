package com.skillora.model;

/**
 * Represents a recruiting company partner in Skillora.
 * Demonstrates: Encapsulation, Constructor Overloading, 'this' keyword.
 */
public class Company {

    private int companyId;
    private String companyName;
    private String industry;
    private String website;
    private String contactEmail;

    public Company() {}

    public Company(String companyName, String industry, String website, String contactEmail) {
        this.companyName = companyName;
        this.industry = industry;
        this.website = website;
        this.contactEmail = contactEmail;
    }

    public Company(int companyId, String companyName, String industry, String website, String contactEmail) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.industry = industry;
        this.website = website;
        this.contactEmail = contactEmail;
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

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - %s", companyId, companyName, industry, website);
    }
}
