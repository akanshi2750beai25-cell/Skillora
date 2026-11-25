package com.skillora.service;

import com.skillora.dao.SkillDAO;
import com.skillora.dao.SkillDAOImpl;
import com.skillora.exception.SkilloraException;
import com.skillora.model.Skill;
import com.skillora.model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentService {

    private final SkillDAO skillDAO;

    public StudentService() {
        this.skillDAO = new SkillDAOImpl();
    }

    public StudentService(SkillDAO skillDAO) {
        this.skillDAO = skillDAO;
    }

    public List<Skill> getCatalogSkills() throws SkilloraException {
        try {
            return skillDAO.getAllSkills();
        } catch (SQLException e) {
            throw new SkilloraException("Failed to fetch skills catalog: " + e.getMessage(), e);
        }
    }

    public List<Skill> getStudentSkills(int studentId) throws SkilloraException {
        try {
            return skillDAO.getSkillsByStudentId(studentId);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to fetch student skills: " + e.getMessage(), e);
        }
    }

    public boolean addSkill(int studentId, int skillId, String proficiency) throws SkilloraException {
        if (proficiency == null || proficiency.trim().isEmpty()) {
            proficiency = "Beginner";
        }
        try {
            return skillDAO.addSkillToStudent(studentId, skillId, proficiency);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to add skill: " + e.getMessage(), e);
        }
    }

    public boolean removeSkill(int studentId, int skillId) throws SkilloraException {
        try {
            return skillDAO.removeSkillFromStudent(studentId, skillId);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to remove skill: " + e.getMessage(), e);
        }
    }

    // Overloaded Method 1: Find peers by skill
    public List<Student> findPeers(String skillName) throws SkilloraException {
        if (skillName == null || skillName.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return skillDAO.findStudentsBySkill(skillName.trim());
        } catch (SQLException e) {
            throw new SkilloraException("Failed to search peers by skill: " + e.getMessage(), e);
        }
    }

    // Overloaded Method 2: Find peers by skill AND college
    public List<Student> findPeers(String skillName, String college) throws SkilloraException {
        List<Student> allBySkill = findPeers(skillName);
        if (college == null || college.trim().isEmpty()) {
            return allBySkill;
        }

        List<Student> filtered = new ArrayList<>();
        for (Student s : allBySkill) {
            if (s.getCollege() != null && s.getCollege().equalsIgnoreCase(college.trim())) {
                filtered.add(s);
            }
        }
        return filtered;
    }
}
