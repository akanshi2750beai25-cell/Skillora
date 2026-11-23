package com.skillora.dao;

import com.skillora.database.DBConnection;
import com.skillora.model.Hackathon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of HackathonDAO.
 * Contributed by: Paras Jain
 */
public class HackathonDAOImpl implements HackathonDAO {

    private Hackathon mapRow(ResultSet rs) throws SQLException {
        return new Hackathon(
                rs.getInt("hackathon_id"),
                rs.getString("title"),
                rs.getString("host_college"),
                rs.getDate("event_date"),
                rs.getString("domain"),
                rs.getString("location"),
                rs.getInt("team_size_limit"),
                rs.getDate("registration_deadline")
        );
    }

    @Override
    public List<Hackathon> getAllHackathons() throws SQLException {
        List<Hackathon> list = new ArrayList<>();
        String sql = "SELECT * FROM hackathons ORDER BY event_date ASC";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<Hackathon> getHackathonsByDomain(String domain) throws SQLException {
        List<Hackathon> list = new ArrayList<>();
        String sql = "SELECT * FROM hackathons WHERE LOWER(domain) LIKE LOWER(?) ORDER BY event_date ASC";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + domain.trim() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Hackathon> getHackathonsByLocation(String location) throws SQLException {
        List<Hackathon> list = new ArrayList<>();
        String sql = "SELECT * FROM hackathons WHERE LOWER(location) LIKE LOWER(?) ORDER BY event_date ASC";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + location.trim() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean addHackathon(Hackathon hackathon) throws SQLException {
        String sql = "INSERT INTO hackathons (title, host_college, event_date, domain, location, team_size_limit, registration_deadline) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = DBConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, hackathon.getTitle());
            stmt.setString(2, hackathon.getHostCollege());
            stmt.setDate(3, hackathon.getEventDate());
            stmt.setString(4, hackathon.getDomain());
            stmt.setString(5, hackathon.getLocation());
            stmt.setInt(6, hackathon.getTeamSizeLimit());
            stmt.setDate(7, hackathon.getRegistrationDeadline());
            return stmt.executeUpdate() > 0;
        }
    }
}
