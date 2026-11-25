package com.skillora.dao;

import com.skillora.model.Skill;
import com.skillora.model.Student;

import java.sql.SQLException;
import java.util.List;

public interface SkillDAO {
    List<Skill> getAllSkills() throws SQLException;
    List<Skill> getSkillsByStudentId(int studentId) throws SQLException;
    boolean addSkillToStudent(int studentId, int skillId, String proficiency) throws SQLException;
    boolean removeSkillFromStudent(int studentId, int skillId) throws SQLException;
    List<Student> findStudentsBySkill(String skillName) throws SQLException;
    boolean insertSkill(Skill skill) throws SQLException;
}
