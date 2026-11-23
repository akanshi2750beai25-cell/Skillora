package com.skillora.main;

import com.skillora.exception.SkilloraException;
import com.skillora.model.Admin;
import com.skillora.model.Company;
import com.skillora.model.CompanyVisit;
import com.skillora.model.Hackathon;
import com.skillora.service.AuthService;
import com.skillora.service.PlacementService;
import com.skillora.utility.ConsoleTheme;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive Console Dashboard for Placement Officer / Admin.
 * Handles company registrations, placement drives calendar, hackathons, and report exporting.
 * Contributed by: Paras Jain
 */
public class AdminMenu {

    private final Scanner scanner;
    private final AuthService authService;
    private final PlacementService placementService;

    public AdminMenu(Scanner scanner, AuthService authService) {
        this.scanner = scanner;
        this.authService = authService;
        this.placementService = new PlacementService();
    }

    public AdminMenu(Scanner scanner, AuthService authService, PlacementService placementService) {
        this.scanner = scanner;
        this.authService = authService;
        this.placementService = placementService;
    }

    public void showDashboard(Admin admin) {
        while (true) {
            ConsoleTheme.printHeader("Placement Officer Dashboard - Skillora");
            System.out.printf("Admin: %s | Staff ID: %s | Dept: %s\n",
                    admin.getName(), admin.getStaffId(), admin.getDepartment());
            ConsoleTheme.printDivider();
            System.out.println("[1] View Placement Admin Profile Details");
            System.out.println("[2] View Registered Partner Companies");
            System.out.println("[3] Register Partner Company");
            System.out.println("[4] View Scheduled Campus Placement Drives");
            System.out.println("[5] Schedule Campus Placement Visit");
            System.out.println("[6] View All Inter-College Hackathons");
            System.out.println("[7] Filter Hackathons by Domain or Location");
            System.out.println("[8] Publish New Hackathon Event");
            System.out.println("[9] Export Placement Drives & Visits Report");
            System.out.println("[10] Logout");
            ConsoleTheme.printDivider();
            System.out.print("Select an option (1-10): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAdminDetails(admin);
                    break;
                case "2":
                    viewCompanies();
                    break;
                case "3":
                    addCompany();
                    break;
                case "4":
                    viewScheduledVisits();
                    break;
                case "5":
                    scheduleVisit();
                    break;
                case "6":
                    viewHackathons();
                    break;
                case "7":
                    filterHackathons();
                    break;
                case "8":
                    publishHackathon();
                    break;
                case "9":
                    exportPlacementReport();
                    break;
                case "10":
                    authService.logout();
                    ConsoleTheme.printInfo("Admin logged out successfully.");
                    return;
                default:
                    ConsoleTheme.printError("Invalid option. Please choose 1-10.");
            }
        }
    }

    private void viewAdminDetails(Admin admin) {
        ConsoleTheme.printSubHeader("Placement Admin Details");
        System.out.println(admin.getDetails());
        System.out.println("Email: " + admin.getEmail());
    }

    private void viewCompanies() {
        ConsoleTheme.printSubHeader("Registered Partner Companies");
        try {
            List<Company> list = placementService.getCompanies();
            if (list.isEmpty()) {
                ConsoleTheme.printInfo("No companies registered yet.");
                return;
            }
            System.out.printf("%-6s | %-22s | %-18s | %-24s | %-20s\n",
                    "ID", "Company Name", "Industry", "Website", "Contact Email");
            System.out.println("--------------------------------------------------------------------------------");
            for (Company c : list) {
                System.out.printf("%-6d | %-22s | %-18s | %-24s | %-20s\n",
                        c.getCompanyId(), c.getCompanyName(), c.getIndustry(),
                        c.getWebsite() != null ? c.getWebsite() : "N/A",
                        c.getContactEmail() != null ? c.getContactEmail() : "N/A");
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private void addCompany() {
        ConsoleTheme.printSubHeader("Register Partner Company");
        try {
            System.out.print("Company Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                ConsoleTheme.printError("Company name cannot be empty.");
                return;
            }
            System.out.print("Industry (e.g. IT, Banking, Auto): ");
            String industry = scanner.nextLine().trim();
            System.out.print("Website: ");
            String website = scanner.nextLine().trim();
            System.out.print("Contact Email: ");
            String email = scanner.nextLine().trim();

            Company c = new Company(name, industry, website, email);
            if (placementService.addCompany(c)) {
                ConsoleTheme.printSuccess("Company '" + name + "' registered successfully!");
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private void viewScheduledVisits() {
        ConsoleTheme.printSubHeader("Scheduled Campus Recruitment Drives");
        try {
            List<CompanyVisit> visits = placementService.getCompanyVisits();
            if (visits.isEmpty()) {
                ConsoleTheme.printInfo("No placement visits scheduled yet.");
                return;
            }
            System.out.printf("%-8s | %-20s | %-18s | %-12s | %-12s | %-20s\n",
                    "Visit ID", "Company", "College", "Drive Type", "Date", "Eligible Branches");
            System.out.println("--------------------------------------------------------------------------------");
            for (CompanyVisit v : visits) {
                System.out.printf("%-8d | %-20s | %-18s | %-12s | %-12s | %-20s\n",
                        v.getVisitId(),
                        v.getCompanyName() != null ? v.getCompanyName() : "N/A",
                        v.getCollegeName(),
                        v.getDriveType(),
                        v.getVisitDate().toString(),
                        v.getEligibleBranches());
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private void scheduleVisit() {
        ConsoleTheme.printSubHeader("Schedule Campus Recruitment Visit");
        try {
            List<Company> companies = placementService.getCompanies();
            if (companies.isEmpty()) {
                ConsoleTheme.printWarning("Please register a company first.");
                return;
            }

            System.out.println("Available Companies:");
            for (Company c : companies) {
                System.out.printf("  [%d] %s (%s)\n", c.getCompanyId(), c.getCompanyName(), c.getIndustry());
            }

            System.out.print("Enter Company ID: ");
            int cId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("College Name: ");
            String college = scanner.nextLine().trim();
            System.out.print("Visit Date (YYYY-MM-DD): ");
            Date date = Date.valueOf(scanner.nextLine().trim());
            System.out.print("Drive Type (On-Campus / Pool-Campus / Virtual): ");
            String type = scanner.nextLine().trim();
            System.out.print("Eligible Branches (comma separated, e.g. CSE, IT): ");
            String branches = scanner.nextLine().trim();

            CompanyVisit v = new CompanyVisit(cId, college, date, type, branches);
            if (placementService.scheduleVisit(v)) {
                ConsoleTheme.printSuccess("Campus visit scheduled successfully for " + date + "!");
            }
        } catch (IllegalArgumentException e) {
            ConsoleTheme.printError("Invalid format entered (e.g. date format must be YYYY-MM-DD).");
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private void viewHackathons() {
        ConsoleTheme.printSubHeader("All Inter-College Hackathons");
        try {
            List<Hackathon> list = placementService.getHackathons();
            displayHackathonList(list);
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private void filterHackathons() {
        ConsoleTheme.printSubHeader("Filter Hackathons");
        System.out.println("[1] Filter by Domain (e.g. AI, FinTech, Web3)");
        System.out.println("[2] Filter by Location / City");
        System.out.print("Select filter type (1-2): ");
        String choice = scanner.nextLine().trim();

        try {
            if ("1".equals(choice)) {
                System.out.print("Enter domain keyword: ");
                String domain = scanner.nextLine().trim();
                List<Hackathon> list = placementService.getHackathonsByDomain(domain);
                displayHackathonList(list);
            } else if ("2".equals(choice)) {
                System.out.print("Enter location keyword: ");
                String loc = scanner.nextLine().trim();
                List<Hackathon> list = placementService.getHackathonsByLocation(loc);
                displayHackathonList(list);
            } else {
                ConsoleTheme.printError("Invalid option.");
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private void displayHackathonList(List<Hackathon> list) {
        if (list.isEmpty()) {
            ConsoleTheme.printInfo("No hackathons found matching criteria.");
            return;
        }
        System.out.printf("%-5s | %-25s | %-18s | %-12s | %-15s | %-12s\n",
                "ID", "Title", "Host", "Event Date", "Domain", "Deadline");
        System.out.println("--------------------------------------------------------------------------------");
        for (Hackathon h : list) {
            System.out.printf("%-5d | %-25s | %-18s | %-12s | %-15s | %-12s\n",
                    h.getHackathonId(), h.getTitle(), h.getHostCollege(),
                    h.getEventDate().toString(), h.getDomain(),
                    h.getRegistrationDeadline().toString());
        }
    }

    private void publishHackathon() {
        ConsoleTheme.printSubHeader("Publish Inter-College Hackathon");
        try {
            System.out.print("Hackathon Title: ");
            String title = scanner.nextLine().trim();
            System.out.print("Host College / Organization: ");
            String host = scanner.nextLine().trim();
            System.out.print("Event Date (YYYY-MM-DD): ");
            Date eventDate = Date.valueOf(scanner.nextLine().trim());
            System.out.print("Domain (e.g. FinTech, AI, HealthTech): ");
            String domain = scanner.nextLine().trim();
            System.out.print("Location / Platform: ");
            String loc = scanner.nextLine().trim();
            System.out.print("Max Team Size (e.g. 4): ");
            int size = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Registration Deadline (YYYY-MM-DD): ");
            Date regDeadline = Date.valueOf(scanner.nextLine().trim());

            Hackathon h = new Hackathon(0, title, host, eventDate, domain, loc, size, regDeadline);
            if (placementService.publishHackathon(h)) {
                ConsoleTheme.printSuccess("Hackathon event published successfully!");
            }
        } catch (IllegalArgumentException e) {
            ConsoleTheme.printError("Invalid date or numerical input.");
        } catch (Exception e) {
            ConsoleTheme.printError("Failed to publish hackathon: " + e.getMessage());
        }
    }

    private void exportPlacementReport() {
        ConsoleTheme.printSubHeader("Export Placement & Drives Report");
        try {
            System.out.print("Enter output filename (default: placement_report.txt): ");
            String filename = scanner.nextLine().trim();
            if (filename.isEmpty()) {
                filename = "placement_report.txt";
            }

            if (placementService.exportPlacementReport(filename)) {
                ConsoleTheme.printSuccess("Report exported successfully to: " + filename);
            } else {
                ConsoleTheme.printError("Failed to export report to file.");
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }
}
