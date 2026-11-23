package com.skillora.model;

import java.sql.Date;

/**
 * Represents an inter-college technical hackathon or coding competition.
 * Demonstrates: Encapsulation, Constructors, Java Date handling.
 */
public class Hackathon {
    private int hackathonId;
    private String title;
    private String hostCollege;
    private Date eventDate;
    private String domain;
    private String location;
    private int teamSizeLimit;
    private Date registrationDeadline;

    public Hackathon() {}

    public Hackathon(int hackathonId, String title, String hostCollege, Date eventDate,
                     String domain, String location, int teamSizeLimit, Date registrationDeadline) {
        this.hackathonId = hackathonId;
        this.title = title;
        this.hostCollege = hostCollege;
        this.eventDate = eventDate;
        this.domain = domain;
        this.location = location;
        this.teamSizeLimit = teamSizeLimit;
        this.registrationDeadline = registrationDeadline;
    }

    public int getHackathonId() { return hackathonId; }
    public void setHackathonId(int hackathonId) { this.hackathonId = hackathonId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getHostCollege() { return hostCollege; }
    public void setHostCollege(String hostCollege) { this.hostCollege = hostCollege; }

    public Date getEventDate() { return eventDate; }
    public void setEventDate(Date eventDate) { this.eventDate = eventDate; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public int getTeamSizeLimit() { return teamSizeLimit; }
    public void setTeamSizeLimit(int teamSizeLimit) { this.teamSizeLimit = teamSizeLimit; }

    public Date getRegistrationDeadline() { return registrationDeadline; }
    public void setRegistrationDeadline(Date registrationDeadline) { this.registrationDeadline = registrationDeadline; }
}
