package com.skillora.main;

import com.skillora.database.DBConnection;
import com.skillora.exception.AuthenticationException;
import com.skillora.exception.SkilloraException;
import com.skillora.model.Admin;
import com.skillora.model.CompanyVisit;
import com.skillora.model.Hackathon;
import com.skillora.model.Student;
import com.skillora.model.User;
import com.skillora.service.AuthService;
import com.skillora.service.PlacementService;
import com.skillora.utility.ConsoleTheme;
import com.skillora.utility.InputValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Main application entry point for the Skillora Platform.
 * Features:
 * - User Authentication & Student Profile Management (Contributed by: Akanshi)
 * - Campus Placement Drives, Hackathons & Admin Panel (Contributed by: Paras Jain)
 */
public class SkilloraApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
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
}
