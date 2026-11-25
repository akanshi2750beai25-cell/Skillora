package com.skillora.model;

/**
 * Represents a standardized technical skill in the catalog, with optional proficiency level.
 * Demonstrates: Encapsulation, Constructor Overloading, 'this' keyword.
 */
public class Skill {

    private int skillId;
    private String skillName;
    private String category; // e.g. "Programming", "Database", "Frontend", "AI"
    private String proficiencyLevel; // "Beginner", "Intermediate", "Advanced"

    public Skill() {}

    // Overloaded Constructor 1 (For creating a new skill definition)
    public Skill(String skillName, String category) {
        this.skillName = skillName;
        this.category = category;
        this.proficiencyLevel = "Beginner";
    }

    // Overloaded Constructor 2 (For skill catalog records)
    public Skill(int skillId, String skillName, String category) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.proficiencyLevel = "Beginner";
    }

    // Overloaded Constructor 3 (For student assigned skills with proficiency)
    public Skill(int skillId, String skillName, String category, String proficiencyLevel) {
        this.skillId = skillId;
        this.skillName = skillName;
        this.category = category;
        this.proficiencyLevel = proficiencyLevel;
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    @Override
    public String toString() {
        return String.format("%s (%s) [%s]", skillName, category, proficiencyLevel != null ? proficiencyLevel : "N/A");
    }
}
