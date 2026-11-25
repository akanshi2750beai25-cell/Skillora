package com.skillora.service;

import com.skillora.dao.CompanyDAO;
import com.skillora.dao.CompanyDAOImpl;
import com.skillora.dao.HackathonDAO;
import com.skillora.dao.HackathonDAOImpl;
import com.skillora.exception.SkilloraException;
import com.skillora.model.Company;
import com.skillora.model.CompanyVisit;
import com.skillora.model.Hackathon;
import com.skillora.utility.FileUtil;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for placement drives, visiting companies, hackathons, and report exporting.
 * Contributed by: Paras Jain
 */
public class PlacementService {

    private final CompanyDAO companyDAO;
    private final HackathonDAO hackathonDAO;

    public PlacementService() {
        this.companyDAO = new CompanyDAOImpl();
        this.hackathonDAO = new HackathonDAOImpl();
    }

    public PlacementService(CompanyDAO companyDAO, HackathonDAO hackathonDAO) {
        this.companyDAO = companyDAO;
        this.hackathonDAO = hackathonDAO;
    }

    public List<Company> getCompanies() throws SkilloraException {
        try {
            return companyDAO.getAllCompanies();
        } catch (SQLException e) {
            throw new SkilloraException("Failed to fetch companies: " + e.getMessage(), e);
        }
    }

    public boolean addCompany(Company company) throws SkilloraException {
        try {
            return companyDAO.addCompany(company);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to add company: " + e.getMessage(), e);
        }
    }

    public List<CompanyVisit> getCompanyVisits() throws SkilloraException {
        try {
            return companyDAO.getAllVisits();
        } catch (SQLException e) {
            throw new SkilloraException("Failed to fetch company visits: " + e.getMessage(), e);
        }
    }

    public boolean scheduleVisit(CompanyVisit visit) throws SkilloraException {
        try {
            return companyDAO.addCompanyVisit(visit);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to schedule visit: " + e.getMessage(), e);
        }
    }

    public List<Hackathon> getHackathons() throws SkilloraException {
        try {
            return hackathonDAO.getAllHackathons();
        } catch (SQLException e) {
            throw new SkilloraException("Failed to fetch hackathons: " + e.getMessage(), e);
        }
    }

    public List<Hackathon> getHackathonsByDomain(String domain) throws SkilloraException {
        try {
            return hackathonDAO.getHackathonsByDomain(domain);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to filter hackathons by domain: " + e.getMessage(), e);
        }
    }

    public List<Hackathon> getHackathonsByLocation(String location) throws SkilloraException {
        try {
            return hackathonDAO.getHackathonsByLocation(location);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to filter hackathons by location: " + e.getMessage(), e);
        }
    }

    public boolean publishHackathon(Hackathon hackathon) throws SkilloraException {
        try {
            return hackathonDAO.addHackathon(hackathon);
        } catch (SQLException e) {
            throw new SkilloraException("Failed to publish hackathon: " + e.getMessage(), e);
        }
    }

    /**
     * Generates a structured placement report and writes it to a file.
     * Demonstrates: File I/O persistence via FileUtil.
     */
    public boolean exportPlacementReport(String filename) throws SkilloraException {
        try {
            List<CompanyVisit> visits = companyDAO.getAllVisits();
            List<Company> companies = companyDAO.getAllCompanies();

            StringBuilder sb = new StringBuilder();
            sb.append("================================================================================\n");
            sb.append("                 SKILLORA CAMPUS PLACEMENT & VISITS REPORT                      \n");
            sb.append("================================================================================\n\n");

            sb.append("1. SCHEDULED RECRUITMENT DRIVES:\n");
            sb.append("--------------------------------------------------------------------------------\n");
            sb.append(String.format("%-8s | %-20s | %-18s | %-12s | %-12s | %-20s\n",
                    "Visit ID", "Company", "College", "Drive Type", "Date", "Eligible Branches"));
            sb.append("--------------------------------------------------------------------------------\n");

            for (CompanyVisit v : visits) {
                sb.append(String.format("%-8d | %-20s | %-18s | %-12s | %-12s | %-20s\n",
                        v.getVisitId(),
                        v.getCompanyName() != null ? v.getCompanyName() : "N/A",
                        v.getCollegeName(),
                        v.getDriveType(),
                        v.getVisitDate().toString(),
                        v.getEligibleBranches()));
            }

            sb.append("\n\n2. REGISTERED PARTNER COMPANIES:\n");
            sb.append("--------------------------------------------------------------------------------\n");
            sb.append(String.format("%-6s | %-22s | %-18s | %-24s | %-20s\n",
                    "ID", "Company Name", "Industry", "Website", "Contact Email"));
            sb.append("--------------------------------------------------------------------------------\n");

            for (Company c : companies) {
                sb.append(String.format("%-6d | %-22s | %-18s | %-24s | %-20s\n",
                        c.getCompanyId(),
                        c.getCompanyName(),
                        c.getIndustry(),
                        c.getWebsite() != null ? c.getWebsite() : "N/A",
                        c.getContactEmail() != null ? c.getContactEmail() : "N/A"));
            }
            sb.append("================================================================================\n");

            return FileUtil.writeReport(filename, sb.toString());
        } catch (SQLException e) {
            throw new SkilloraException("Failed to generate placement report: " + e.getMessage(), e);
        }
    }

    /**
     * Generates a structured hackathons directory report and writes it to a file.
     */
    public boolean exportHackathonsReport(String filename) throws SkilloraException {
        try {
            List<Hackathon> list = hackathonDAO.getAllHackathons();

            StringBuilder sb = new StringBuilder();
            sb.append("================================================================================\n");
            sb.append("                   SKILLORA INTER-COLLEGE HACKATHONS DIRECTORY                  \n");
            sb.append("================================================================================\n");
            sb.append(String.format("%-5s | %-25s | %-18s | %-12s | %-15s | %-12s\n",
                    "ID", "Title", "Host", "Event Date", "Domain", "Deadline"));
            sb.append("--------------------------------------------------------------------------------\n");

            for (Hackathon h : list) {
                sb.append(String.format("%-5d | %-25s | %-18s | %-12s | %-15s | %-12s\n",
                        h.getHackathonId(),
                        h.getTitle(),
                        h.getHostCollege(),
                        h.getEventDate().toString(),
                        h.getDomain(),
                        h.getRegistrationDeadline().toString()));
            }
            sb.append("================================================================================\n");

            return FileUtil.writeReport(filename, sb.toString());
        } catch (SQLException e) {
            throw new SkilloraException("Failed to generate hackathon report: " + e.getMessage(), e);
        }
    }
}
