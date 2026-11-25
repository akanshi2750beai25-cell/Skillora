package com.skillora.dao;

import com.skillora.database.DBConnection;
import com.skillora.model.Skill;
import com.skillora.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SkillDAOImpl implements SkillDAO {

    @Override
    public List<Skill> getAllSkills() throws SQLException {
        List<Skill> list = new ArrayList<>();
        String sql = "SELECT * FROM skills ORDER BY category, skill_name";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Skill(
                        rs.getInt("skill_id"),
                        rs.getString("skill_name"),
                        rs.getString("category")
                ));
            }
        }
        return list;
    }

    @Override
    public List<Skill> getSkillsByStudentId(int studentId) throws SQLException {
        List<Skill> list = new ArrayList<>();
        String sql = "SELECT s.skill_id, s.skill_name, s.category, ss.proficiency_level " +
                     "FROM skills s JOIN student_skills ss ON s.skill_id = ss.skill_id " +
                     "WHERE ss.student_id = ? ORDER BY s.skill_name";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Skill(
                            rs.getInt("skill_id"),
                            rs.getString("skill_name"),
                            rs.getString("category"),
                            rs.getString("proficiency_level")
                    ));
                }
            }
        }
        return list;
    }

    @Override
    public boolean addSkillToStudent(int studentId, int skillId, String proficiency) throws SQLException {
        String sql = "INSERT INTO student_skills (student_id, skill_id, proficiency_level) " +
                     "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE proficiency_level = ?";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, skillId);
            stmt.setString(3, proficiency);
            stmt.setString(4, proficiency);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean removeSkillFromStudent(int studentId, int skillId) throws SQLException {
        String sql = "DELETE FROM student_skills WHERE student_id = ? AND skill_id = ?";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, skillId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public List<Student> findStudentsBySkill(String skillName) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT u.user_id, u.name, u.email, u.created_at, " +
                     "s.roll_number, s.college, s.branch, s.semester, s.cgpa, " +
                     "sk.skill_id, sk.skill_name, sk.category, ss.proficiency_level " +
                     "FROM users u " +
                     "JOIN students s ON u.user_id = s.student_id " +
                     "JOIN student_skills ss ON s.student_id = ss.student_id " +
                     "JOIN skills sk ON ss.skill_id = sk.skill_id " +
                     "WHERE LOWER(sk.skill_name) = LOWER(?) ORDER BY s.cgpa DESC";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, skillName.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            "",
                            rs.getTimestamp("created_at"),
                            rs.getString("roll_number"),
                            rs.getString("college"),
                            rs.getString("branch"),
                            rs.getInt("semester"),
                            rs.getDouble("cgpa")
                    );
                    Skill skill = new Skill(
                            rs.getInt("skill_id"),
                            rs.getString("skill_name"),
                            rs.getString("category"),
                            rs.getString("proficiency_level")
                    );
                    student.addSkill(skill);
                    list.add(student);
                }
            }
        }
        return list;
    }

    @Override
    public boolean insertSkill(Skill skill) throws SQLException {
        String sql = "INSERT INTO skills (skill_name, category) VALUES (?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, skill.getSkillName());
            stmt.setString(2, skill.getCategory());
            return stmt.executeUpdate() > 0;
        }
    }
}
