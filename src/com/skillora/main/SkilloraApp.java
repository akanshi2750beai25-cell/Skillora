package com.skillora.main;

import com.skillora.database.DBConnection;
import com.skillora.exception.AuthenticationException;
import com.skillora.exception.DuplicateApplicationException;
import com.skillora.exception.IneligibleStudentException;
import com.skillora.exception.SkilloraException;
import com.skillora.model.Admin;
import com.skillora.model.Application;
import com.skillora.model.Internship;
import com.skillora.model.Student;
import com.skillora.model.User;
import com.skillora.service.AuthService;
import com.skillora.service.InternshipService;
import com.skillora.model.CompanyVisit;
import com.skillora.model.Hackathon;
import com.skillora.model.Student;
import com.skillora.model.User;
import com.skillora.service.AuthService;
import com.skillora.service.PlacementService;
import com.skillora.utility.ConsoleTheme;
import com.skillora.utility.InputValidator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Main application entry point for the Skillora Platform.
 * Contributed by: Akanshi (User Authentication & Profile Management)
 * Integrated with: Navdeep Randhawa (Internship Opportunities & Application Management)
 * Features:
 * - User Authentication & Student Profile Management (Contributed by: Akanshi)
 * - Campus Placement Drives, Hackathons & Admin Panel (Contributed by: Paras Jain)
 */
public class SkilloraApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final InternshipService internshipService = new InternshipService();
    private static final PlacementService placementService = new PlacementService();

    public static void main(String[] args) {
        ConsoleTheme.printBanner();

        // Check database connection on startup
        try {
            Connection testConn = DBConnection.getConnection();
            if (testConn != null && !testConn.isClosed()) {
                ConsoleTheme.printSuccess("Database connected successfully (MySQL skillora_db).");
            }
        } catch (SQLException e) {
            ConsoleTheme.printWarning("Database connection issue: " + e.getMessage());
            ConsoleTheme.printInfo("Ensure MySQL server is running and 'resources/db.properties' has valid credentials.");
        }

        boolean running = true;
        while (running) {
            ConsoleTheme.printHeader("Main Portal - Welcome to Skillora");
            System.out.println("[1] Login (Student / Admin)");
            System.out.println("[2] Register New Student");
            System.out.println("[3] Exit Application");
            ConsoleTheme.printDivider();
            System.out.print("Enter your choice (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegistration();
                    break;
                case "3":
                    running = false;
                    ConsoleTheme.printInfo("Thank you for using Skillora. Have a great career ahead!");
                    break;
                default:
                    ConsoleTheme.printError("Invalid option. Please enter 1, 2, or 3.");
            }
        }

        DBConnection.closeConnection();
        scanner.close();
    }

    private static void handleLogin() {
        ConsoleTheme.printSubHeader("User Authentication");
        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine().trim();

        try {
            User user = authService.login(email, password);
            ConsoleTheme.printSuccess("Welcome back, " + user.getName() + "!");

            if ("STUDENT".equalsIgnoreCase(user.getRole()) && user instanceof Student) {
                showStudentDashboard((Student) user);
            } else if ("ADMIN".equalsIgnoreCase(user.getRole()) && user instanceof Admin) {
                AdminMenu adminMenu = new AdminMenu(scanner, authService, placementService);
                adminMenu.showDashboard((Admin) user);
            }
        } catch (AuthenticationException e) {
            ConsoleTheme.printWarning(e.getMessage());
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private static void showStudentDashboard(Student student) {
        boolean inDashboard = true;
        while (inDashboard) {
            ConsoleTheme.printHeader("Student Dashboard - Skillora");
            System.out.printf("Logged in: %s | Roll: %s | CGPA: %.2f | Semester: %d\n",
                    student.getName(), student.getRollNumber(), student.getCgpa(), student.getSemester());
            ConsoleTheme.printDivider();
            System.out.println("[1] View My Profile");
            System.out.println("[2] Update My Profile");
            System.out.println("[3] Browse & Apply for Internships (Navdeep's Contribution)");
            System.out.println("[4] View My Internship Applications (Navdeep's Contribution)");
            System.out.println("[3] View Upcoming Campus Placement Drives");
            System.out.println("[4] Explore Inter-College Hackathons");
            System.out.println("[5] Logout");
            ConsoleTheme.printDivider();
            System.out.print("Select an option (1-5): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewProfile(student);
                    break;
                case "2":
                    updateProfile(student);
                    break;
                case "3":
                    browseAndApplyInternships(student);
                    break;
                case "4":
                    viewMyApplications(student);
                    break;
                case "5":
                    authService.logout();
                    ConsoleTheme.printInfo("You have been logged out successfully.");
                    inDashboard = false;
                    break;
                default:
                    ConsoleTheme.printError("Invalid choice. Please select 1-5.");
            }
        }
    }

    private static void showAdminDashboard(Admin admin) {
        boolean inDashboard = true;
        while (inDashboard) {
            ConsoleTheme.printHeader("Administrator Dashboard - Skillora");
            System.out.printf("Admin: %s | Staff ID: %s | Dept: %s\n",
                    admin.getName(), admin.getStaffId(), admin.getDepartment());
            ConsoleTheme.printDivider();
            System.out.println("[1] View Admin Details");
            System.out.println("[2] Post New Internship Opportunity (Navdeep's Contribution)");
            System.out.println("[3] View All Internship Applications (Navdeep's Contribution)");
            System.out.println("[4] Logout");
            ConsoleTheme.printDivider();
            System.out.print("Select an option (1-4): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    ConsoleTheme.printSubHeader("Placement Admin Details");
                    System.out.println(admin.getDetails());
                    System.out.println("Email: " + admin.getEmail());
                    break;
                case "2":
                    postInternship();
                    break;
                case "3":
                    viewAllApplications();
                    break;
                case "4":
                    viewUpcomingVisits();
                    break;
                case "4":
                    exploreHackathons();
                    break;
                case "5":
                    authService.logout();
                    ConsoleTheme.printInfo("You have been logged out successfully.");
                    inDashboard = false;
                    break;
                default:
                    ConsoleTheme.printError("Invalid option. Please choose 1-4.");
                    ConsoleTheme.printError("Invalid choice. Please select 1-5.");
            }
        }
    }

    private static void viewProfile(Student student) {
        ConsoleTheme.printSubHeader("My Student Profile");
        System.out.println(student.getDetails());
        System.out.println("Email: " + student.getEmail());
        if (student.getCreatedAt() != null) {
            System.out.println("Member Since: " + student.getCreatedAt());
        }
    }

    private static void updateProfile(Student student) {
        ConsoleTheme.printSubHeader("Update Student Profile Details");
        System.out.println("Leave blank to keep current value.");

        System.out.print("Enter Updated Name [" + student.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) student.setName(name);

        System.out.print("Enter Updated College [" + student.getCollege() + "]: ");
        String college = scanner.nextLine().trim();
        if (!college.isEmpty()) student.setCollege(college);

        System.out.print("Enter Updated Branch [" + student.getBranch() + "]: ");
        String branch = scanner.nextLine().trim();
        if (!branch.isEmpty()) student.setBranch(branch);

        System.out.print("Enter Current Semester (1-12) [" + student.getSemester() + "]: ");
        String semStr = scanner.nextLine().trim();
        if (!semStr.isEmpty()) {
            try {
                int sem = Integer.parseInt(semStr);
                if (InputValidator.isValidSemester(sem)) {
                    student.setSemester(sem);
                } else {
                    ConsoleTheme.printWarning("Invalid semester range (1-12). Keeping previous value.");
                }
            } catch (NumberFormatException e) {
                ConsoleTheme.printWarning("Invalid number format. Keeping previous value.");
            }
        }

        System.out.print("Enter Current CGPA (0.00-10.00) [" + student.getCgpa() + "]: ");
        String cgpaStr = scanner.nextLine().trim();
        if (!cgpaStr.isEmpty()) {
            try {
                double cgpa = Double.parseDouble(cgpaStr);
                if (InputValidator.isValidCgpa(cgpa)) {
                    student.setCgpa(cgpa);
                } else {
                    ConsoleTheme.printWarning("Invalid CGPA range (0.00-10.00). Keeping previous value.");
                }
            } catch (NumberFormatException e) {
                ConsoleTheme.printWarning("Invalid number format. Keeping previous value.");
            }
        }

        try {
            if (authService.updateProfile(student)) {
                ConsoleTheme.printSuccess("Profile updated successfully!");
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private static void viewUpcomingVisits() {
        ConsoleTheme.printSubHeader("Upcoming Campus Placement Drives");
        try {
            List<CompanyVisit> visits = placementService.getCompanyVisits();
            if (visits.isEmpty()) {
                ConsoleTheme.printInfo("No campus recruitment drives scheduled currently.");
                return;
            }
            System.out.printf("%-20s | %-18s | %-12s | %-12s | %-20s\n",
                    "Company", "College", "Drive Type", "Date", "Eligible Branches");
            System.out.println("--------------------------------------------------------------------------------");
            for (CompanyVisit v : visits) {
                System.out.printf("%-20s | %-18s | %-12s | %-12s | %-20s\n",
                        v.getCompanyName() != null ? v.getCompanyName() : "N/A",
                        v.getCollegeName(),
                        v.getDriveType(),
                        v.getVisitDate().toString(),
                        v.getEligibleBranches());
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError("Could not fetch placement drives: " + e.getMessage());
        }
    }

    private static void exploreHackathons() {
        ConsoleTheme.printSubHeader("Inter-College Hackathons & Competitions");
        try {
            List<Hackathon> list = placementService.getHackathons();
            if (list.isEmpty()) {
                ConsoleTheme.printInfo("No hackathons listed at this moment.");
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
        } catch (SkilloraException e) {
            ConsoleTheme.printError("Could not fetch hackathons: " + e.getMessage());
        }
    }

    private static void handleRegistration() {
        ConsoleTheme.printSubHeader("Student Registration Form");
        try {
            System.out.print("Full Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Email Address: ");
            String email = scanner.nextLine().trim();
            System.out.print("Password (at least 4 chars): ");
            String password = scanner.nextLine().trim();
            System.out.print("University Roll Number: ");
            String rollNumber = scanner.nextLine().trim();
            System.out.print("College / University Name: ");
            String college = scanner.nextLine().trim();
            System.out.print("Academic Branch (e.g. Computer Science, IT): ");
            String branch = scanner.nextLine().trim();
            System.out.print("Current Semester (1-8): ");
            int semester = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Current CGPA (0.00 - 10.00): ");
            double cgpa = Double.parseDouble(scanner.nextLine().trim());

            Student newStudent = new Student(name, email, password, rollNumber, college, branch, semester, cgpa);

            if (authService.register(newStudent)) {
                ConsoleTheme.printSuccess("Registration successful! You can now log in with your credentials.");
            }
        } catch (NumberFormatException e) {
            ConsoleTheme.printError("Invalid numerical input for Semester or CGPA. Registration cancelled.");
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    // =========================================================================
    // Integrated Navdeep Randhawa's Contribution: Internship & Applications
    // =========================================================================

    private static void browseAndApplyInternships(Student student) {
        ConsoleTheme.printSubHeader("Internship Opportunities");
        try {
            List<Internship> list = internshipService.getAllOpenInternships();
            if (list == null || list.isEmpty()) {
                ConsoleTheme.printInfo("No internships currently open.");
                return;
            }

            for (Internship i : list) {
                boolean eligible = internshipService.isStudentEligible(student, i);
                String badge = eligible ? ConsoleTheme.GREEN + "[ELIGIBLE]" + ConsoleTheme.RESET
                                        : ConsoleTheme.YELLOW + "[INELIGIBLE]" + ConsoleTheme.RESET;
                System.out.printf("[%d] %s at %s %s\n", i.getId(), i.getTitle(), i.getCompanyName(), badge);
                System.out.printf("    Stipend: ₹%,.2f | Min CGPA: %.2f | Min Sem: %d | Branches: %s\n",
                        i.getStipend(), i.getMinCgpa(), i.getMinSemester(), i.getEligibleBranches());
                System.out.printf("    Required Skills: %s | Deadline: %s\n", i.getRequiredSkills(), i.getDeadline());
                // Polymorphic invocation of Opportunity.displayDetails()
                System.out.println("    Details:");
                i.displayDetails();
            }

            System.out.println("\n[A] Apply for an Internship by ID");
            System.out.println("[B] Back to Dashboard");
            System.out.print("Choice: ");
            String ans = scanner.nextLine().trim().toUpperCase();

            if ("A".equals(ans)) {
                System.out.print("Enter Internship ID to apply: ");
                try {
                    int id = Integer.parseInt(scanner.nextLine().trim());
                    if (internshipService.apply(student, id)) {
                        ConsoleTheme.printSuccess("Application submitted successfully!");
                    }
                } catch (NumberFormatException e) {
                    ConsoleTheme.printError("Invalid Internship ID format.");
                } catch (DuplicateApplicationException | IneligibleStudentException e) {
                    ConsoleTheme.printWarning(e.getMessage());
                } catch (SkilloraException e) {
                    ConsoleTheme.printError(e.getMessage());
                }
            }
        } catch (Exception e) {
            ConsoleTheme.printError("Error loading internships: " + e.getMessage());
        }
    }

    private static void viewMyApplications(Student student) {
        ConsoleTheme.printSubHeader("My Submitted Applications");
        try {
            List<Application> apps = internshipService.getStudentApplications(student.getUserId());
            if (apps == null || apps.isEmpty()) {
                ConsoleTheme.printInfo("You have not applied to any internships yet.");
                return;
            }

            System.out.printf("%-8s | %-25s | %-18s | %-12s | %-15s\n", "App ID", "Role", "Company", "Status", "Date");
            ConsoleTheme.printDivider();
            for (Application a : apps) {
                String dateStr = a.getApplicationDate() != null ? a.getApplicationDate().toString().substring(0, 10) : "N/A";
                System.out.printf("%-8d | %-25s | %-18s | %-12s | %-15s\n",
                        a.getApplicationId(),
                        a.getInternshipTitle() != null ? a.getInternshipTitle() : "N/A",
                        a.getCompanyName() != null ? a.getCompanyName() : "N/A",
                        a.getStatus(),
                        dateStr);
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private static void postInternship() {
        ConsoleTheme.printSubHeader("Post New Internship Opportunity");
        try {
            System.out.print("Enter Company ID (numeric): ");
            int cId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Role / Title: ");
            String title = scanner.nextLine().trim();
            System.out.print("Description: ");
            String desc = scanner.nextLine().trim();
            System.out.print("Monthly Stipend (₹): ");
            double stipend = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Duration (Months): ");
            int duration = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Location: ");
            String loc = scanner.nextLine().trim();
            System.out.print("Minimum CGPA Cutoff (e.g. 7.50): ");
            double minCgpa = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Eligible Branches (e.g. Computer Science, IT): ");
            String branches = scanner.nextLine().trim();
            System.out.print("Minimum Semester (e.g. 4): ");
            int minSem = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Required Skills (e.g. Java, MySQL): ");
            String skills = scanner.nextLine().trim();
            System.out.print("Application Deadline (YYYY-MM-DD): ");
            Date deadline = Date.valueOf(scanner.nextLine().trim());

            Internship intern = new Internship(0, title, cId, desc, deadline, "OPEN",
                    stipend, duration, loc, minCgpa, branches, minSem, skills);

            if (internshipService.createInternship(intern)) {
                ConsoleTheme.printSuccess("Internship opening posted successfully!");
            }
        } catch (Exception e) {
            ConsoleTheme.printError("Failed to post internship: " + e.getMessage());
        }
    }

    private static void viewAllApplications() {
        ConsoleTheme.printSubHeader("All Student Applications");
        try {
            List<Application> list = internshipService.getAllApplications();
            if (list == null || list.isEmpty()) {
                ConsoleTheme.printInfo("No student applications submitted yet.");
                return;
            }

            System.out.printf("%-8s | %-12s | %-25s | %-20s | %-12s\n",
                    "App ID", "Student ID", "Role", "Company", "Status");
            ConsoleTheme.printDivider();
            for (Application a : list) {
                System.out.printf("%-8d | %-12d | %-25s | %-20s | %-12s\n",
                        a.getApplicationId(), a.getStudentId(),
                        a.getInternshipTitle() != null ? a.getInternshipTitle() : "N/A",
                        a.getCompanyName() != null ? a.getCompanyName() : "N/A",
                        a.getStatus());
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }
}
