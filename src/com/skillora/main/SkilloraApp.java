package com.skillora.main;

import com.skillora.database.DBConnection;
import com.skillora.exception.AuthenticationException;
import com.skillora.exception.SkilloraException;
import com.skillora.model.Admin;
import com.skillora.model.Student;
import com.skillora.model.User;
import com.skillora.service.AuthService;
import com.skillora.utility.ConsoleTheme;
import com.skillora.utility.InputValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Main application entry point for the Skillora Authentication & Student Profile Management Module.
 * Contributed by: Akanshi
 */
public class SkilloraApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();

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
                showAdminDashboard((Admin) user);
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
            System.out.println("[3] Logout");
            ConsoleTheme.printDivider();
            System.out.print("Select an option (1-3): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewProfile(student);
                    break;
                case "2":
                    updateProfile(student);
                    break;
                case "3":
                    authService.logout();
                    ConsoleTheme.printInfo("You have been logged out successfully.");
                    inDashboard = false;
                    break;
                default:
                    ConsoleTheme.printError("Invalid choice. Please select 1-3.");
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
            System.out.println("[2] Logout");
            ConsoleTheme.printDivider();
            System.out.print("Select an option (1-2): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    ConsoleTheme.printSubHeader("Placement Admin Details");
                    System.out.println(admin.getDetails());
                    System.out.println("Email: " + admin.getEmail());
                    break;
                case "2":
                    authService.logout();
                    ConsoleTheme.printInfo("Admin logged out successfully.");
                    inDashboard = false;
                    break;
                default:
                    ConsoleTheme.printError("Invalid option. Please choose 1-2.");
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
