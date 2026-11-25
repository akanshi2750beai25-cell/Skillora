package com.skillora.main;

import com.skillora.exception.DuplicateApplicationException;
import com.skillora.exception.IneligibleStudentException;
import com.skillora.exception.SkilloraException;
import com.skillora.model.Application;
import com.skillora.model.CompanyVisit;
import com.skillora.model.Hackathon;
import com.skillora.model.Internship;
import com.skillora.model.Skill;
import com.skillora.model.Student;
import com.skillora.service.AuthService;
import com.skillora.service.InternshipService;
import com.skillora.service.PlacementService;
import com.skillora.service.StudentService;
import com.skillora.utility.ConsoleTheme;
import com.skillora.utility.InputValidator;

import java.util.List;
import java.util.Scanner;

public class StudentMenu {

    private final Scanner scanner;
    private final AuthService authService;
    private final StudentService studentService;
    private final InternshipService internshipService;
    private final PlacementService placementService;

    public StudentMenu(Scanner scanner, AuthService authService) {
        this.scanner = scanner;
        this.authService = authService;
        this.studentService = new StudentService();
        this.internshipService = new InternshipService();
        this.placementService = new PlacementService();
    }

    public void showDashboard(Student student) {
        while (true) {
            ConsoleTheme.printHeader("Student Dashboard - Skillora");
            System.out.printf("Logged in: %s | Roll: %s | CGPA: %.2f | Semester: %d\n",
                    student.getName(), student.getRollNumber(), student.getCgpa(), student.getSemester());
            ConsoleTheme.printDivider();
            System.out.println("[1] View & Update My Profile");
            System.out.println("[2] Manage My Technical Skills");
            System.out.println("[3] Browse & Apply for Internships");
            System.out.println("[4] View Campus Placement Drives");
            System.out.println("[5] Explore Inter-College Hackathons");
            System.out.println("[6] Peer Networking (Find Teammates by Skill)");
            System.out.println("[7] View My Submitted Applications");
            System.out.println("[8] Logout");
            ConsoleTheme.printDivider();
            System.out.print("Select an option (1-8): ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewAndUpdateProfile(student);
                    break;
                case "2":
                    manageSkills(student);
                    break;
                case "3":
                    browseAndApplyInternships(student);
                    break;
                case "4":
                    viewCompanyVisits();
                    break;
                case "5":
                    exploreHackathons();
                    break;
                case "6":
                    networkPeers();
                    break;
                case "7":
                    viewMyApplications(student);
                    break;
                case "8":
                    authService.logout();
                    ConsoleTheme.printInfo("You have been logged out successfully.");
                    return;
                default:
                    ConsoleTheme.printError("Invalid choice. Please select 1-8.");
            }
        }
    }

    private void viewAndUpdateProfile(Student student) {
        ConsoleTheme.printSubHeader("My Profile Details");
        System.out.println(student.getDetails());
        System.out.println("Email: " + student.getEmail());

        System.out.print("\nDo you want to update basic details? (y/n): ");
        String ans = scanner.nextLine().trim();
        if ("y".equalsIgnoreCase(ans)) {
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
                    if (InputValidator.isValidSemester(sem)) student.setSemester(sem);
                } catch (NumberFormatException ignored) {}
            }

            System.out.print("Enter Current CGPA (0.00-10.00) [" + student.getCgpa() + "]: ");
            String cgpaStr = scanner.nextLine().trim();
            if (!cgpaStr.isEmpty()) {
                try {
                    double cgpa = Double.parseDouble(cgpaStr);
                    if (InputValidator.isValidCgpa(cgpa)) student.setCgpa(cgpa);
                } catch (NumberFormatException ignored) {}
            }

            try {
                if (authService.updateProfile(student)) {
                    ConsoleTheme.printSuccess("Profile updated successfully!");
                }
            } catch (SkilloraException e) {
                ConsoleTheme.printError(e.getMessage());
            }
        }
    }

    private void manageSkills(Student student) {
        while (true) {
            ConsoleTheme.printSubHeader("My Technical Skills");
            try {
                List<Skill> mySkills = studentService.getStudentSkills(student.getUserId());
                if (mySkills.isEmpty()) {
                    ConsoleTheme.printInfo("You have not added any technical skills yet.");
                } else {
                    System.out.printf("%-8s | %-20s | %-18s | %-12s\n", "Skill ID", "Skill Name", "Category", "Proficiency");
                    System.out.println("------------------------------------------------------------------");
                    for (Skill s : mySkills) {
                        System.out.printf("%-8d | %-20s | %-18s | %-12s\n",
                                s.getSkillId(), s.getSkillName(), s.getCategory(), s.getProficiencyLevel());
                    }
                }

                System.out.println("\n[A] Add a Skill from Catalog");
                System.out.println("[R] Remove a Skill");
                System.out.println("[B] Back to Dashboard");
                System.out.print("Choice: ");
                String action = scanner.nextLine().trim().toUpperCase();

                if ("B".equals(action)) return;

                if ("A".equals(action)) {
                    List<Skill> catalog = studentService.getCatalogSkills();
                    System.out.println("\n--- Available Skills in Catalog ---");
                    for (Skill s : catalog) {
                        System.out.printf("[%d] %s (%s)\n", s.getSkillId(), s.getSkillName(), s.getCategory());
                    }
                    System.out.print("Enter Skill ID to add: ");
                    int sId = Integer.parseInt(scanner.nextLine().trim());

                    System.out.print("Enter Proficiency (Beginner / Intermediate / Advanced): ");
                    String level = scanner.nextLine().trim();
                    if (!level.equalsIgnoreCase("Intermediate") && !level.equalsIgnoreCase("Advanced")) {
                        level = "Beginner";
                    }

                    if (studentService.addSkill(student.getUserId(), sId, level)) {
                        ConsoleTheme.printSuccess("Skill added to your profile!");
                    }
                } else if ("R".equals(action)) {
                    System.out.print("Enter Skill ID to remove: ");
                    int sId = Integer.parseInt(scanner.nextLine().trim());
                    if (studentService.removeSkill(student.getUserId(), sId)) {
                        ConsoleTheme.printSuccess("Skill removed from your profile.");
                    }
                }
            } catch (Exception e) {
                ConsoleTheme.printError(e.getMessage());
            }
        }
    }

    private void browseAndApplyInternships(Student student) {
        ConsoleTheme.printSubHeader("Internship Opportunities");
        try {
            List<Internship> list = internshipService.getAllOpenInternships();
            if (list.isEmpty()) {
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
                System.out.printf("    Required Skills: %s | Deadline: %s\n\n", i.getRequiredSkills(), i.getDeadline());
            }

            System.out.println("[A] Apply for an Internship by ID");
            System.out.println("[B] Back to Dashboard");
            System.out.print("Choice: ");
            String ans = scanner.nextLine().trim().toUpperCase();

            if ("A".equals(ans)) {
                System.out.print("Enter Internship ID: ");
                int id = Integer.parseInt(scanner.nextLine().trim());

                try {
                    if (internshipService.apply(student, id)) {
                        ConsoleTheme.printSuccess("Application submitted successfully!");
                    }
                } catch (DuplicateApplicationException | IneligibleStudentException e) {
                    ConsoleTheme.printWarning(e.getMessage());
                } catch (SkilloraException e) {
                    ConsoleTheme.printError(e.getMessage());
                }
            }
        } catch (Exception e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private void viewCompanyVisits() {
        ConsoleTheme.printSubHeader("Upcoming Campus Recruitment Visits");
        try {
            List<CompanyVisit> visits = placementService.getCompanyVisits();
            if (visits.isEmpty()) {
                ConsoleTheme.printInfo("No campus visits scheduled at this time.");
                return;
            }

            System.out.printf("%-18s | %-12s | %-12s | %-25s\n", "Company", "Visit Date", "Drive Type", "Eligible Branches");
            System.out.println("--------------------------------------------------------------------------------");
            for (CompanyVisit v : visits) {
                System.out.printf("%-18s | %-12s | %-12s | %-25s\n",
                        v.getCompanyName(), v.getVisitDate(), v.getDriveType(), v.getEligibleBranches());
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private void exploreHackathons() {
        ConsoleTheme.printSubHeader("Explore Inter-College Hackathons");
        System.out.println("[1] View All Hackathons");
        System.out.println("[2] Filter by Domain (e.g. AI, Web3, FinTech)");
        System.out.println("[3] Filter by Location (e.g. Delhi, Chandigarh)");
        System.out.print("Select: ");
        String choice = scanner.nextLine().trim();

        try {
            List<Hackathon> list;
            if ("2".equals(choice)) {
                System.out.print("Enter Domain keyword: ");
                String dom = scanner.nextLine().trim();
                list = placementService.getHackathonsByDomain(dom);
            } else if ("3".equals(choice)) {
                System.out.print("Enter Location keyword: ");
                String loc = scanner.nextLine().trim();
                list = placementService.getHackathonsByLocation(loc);
            } else {
                list = placementService.getHackathons();
            }

            if (list.isEmpty()) {
                ConsoleTheme.printInfo("No hackathons matched your query.");
                return;
            }

            System.out.println("\n--------------------------------------------------------------------------------");
            for (Hackathon h : list) {
                System.out.printf("[%d] %s\n", h.getHackathonId(), h.getTitle());
                System.out.printf("    Host: %s | Domain: %s | Location: %s\n", h.getHostCollege(), h.getDomain(), h.getLocation());
                System.out.printf("    Event Date: %s | Reg Deadline: %s | Team Limit: %d\n\n",
                        h.getEventDate(), h.getRegistrationDeadline(), h.getTeamSizeLimit());
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private void networkPeers() {
        ConsoleTheme.printSubHeader("Find Students by Technical Skill (Hackathon Teammate Search)");
        System.out.print("Enter technical skill to search (e.g. Java, Python, React, Flutter): ");
        String skill = scanner.nextLine().trim();

        try {
            List<Student> peers = studentService.findPeers(skill);
            if (peers.isEmpty()) {
                ConsoleTheme.printInfo("No students currently registered with skill: " + skill);
                return;
            }

            System.out.printf("\nFound %d student(s) with skill '%s':\n", peers.size(), skill);
            System.out.printf("%-10s | %-18s | %-20s | %-15s | %-8s\n", "Roll No", "Name", "College", "Branch", "CGPA");
            System.out.println("--------------------------------------------------------------------------------");
            for (Student p : peers) {
                System.out.printf("%-10s | %-18s | %-20s | %-15s | %-8.2f\n",
                        p.getRollNumber(), p.getName(), p.getCollege(), p.getBranch(), p.getCgpa());
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }

    private void viewMyApplications(Student student) {
        ConsoleTheme.printSubHeader("My Submitted Applications");
        try {
            List<Application> apps = internshipService.getStudentApplications(student.getUserId());
            if (apps.isEmpty()) {
                ConsoleTheme.printInfo("You have not applied to any internships yet.");
                return;
            }

            System.out.printf("%-8s | %-25s | %-18s | %-12s | %-15s\n", "App ID", "Role", "Company", "Status", "Date");
            System.out.println("--------------------------------------------------------------------------------");
            for (Application a : apps) {
                System.out.printf("%-8d | %-25s | %-18s | %-12s | %-15s\n",
                        a.getApplicationId(), a.getInternshipTitle(), a.getCompanyName(), a.getStatus(),
                        a.getApplicationDate().toString().substring(0, 10));
            }
        } catch (SkilloraException e) {
            ConsoleTheme.printError(e.getMessage());
        }
    }
}
