package com.skillora.main;

import com.skillora.database.DBConnection;
import com.skillora.exception.AuthenticationException;
import com.skillora.exception.SkilloraException;
import com.skillora.model.Admin;
import com.skillora.model.Student;
import com.skillora.model.User;
import com.skillora.service.AuthService;
import com.skillora.utility.ConsoleTheme;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

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
                StudentMenu studentMenu = new StudentMenu(scanner, authService);
                studentMenu.showDashboard((Student) user);
            } else if ("ADMIN".equalsIgnoreCase(user.getRole()) && user instanceof Admin) {
                AdminMenu adminMenu = new AdminMenu(scanner, authService);
                adminMenu.showDashboard((Admin) user);
            }
        } catch (AuthenticationException e) {
            ConsoleTheme.printWarning(e.getMessage());
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
