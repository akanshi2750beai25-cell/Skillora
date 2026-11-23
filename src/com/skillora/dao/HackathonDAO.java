package com.skillora.dao;

import com.skillora.model.Hackathon;

import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Hackathons.
 * Contributed by: Paras Jain
 */
public interface HackathonDAO {
    List<Hackathon> getAllHackathons() throws SQLException;
    List<Hackathon> getHackathonsByDomain(String domain) throws SQLException;
    List<Hackathon> getHackathonsByLocation(String location) throws SQLException;
    boolean addHackathon(Hackathon hackathon) throws SQLException;
}
