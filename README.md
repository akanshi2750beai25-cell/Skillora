# Skillora – Student Registration, Authentication & Internship Platform

A modular, production-grade console application built using **Core Java OOP** and **MySQL (JDBC)**, created for the university evaluation of course **Programming in Java (25CSE0202)**.

This repository combines the verified contributions of:
* **Akanshi:** User Authentication, Student Registration & Profile Management
* **Navdeep Randhawa:** Internship Opportunities, Eligibility Verification & Application Management

---

## 1. Module Overview & Features

### Core Capabilities:
1. **Student Registration & Profile Management (Akanshi):**
   * Validates name, university roll number, email address, password strength, academic branch, semester (1–12), and CGPA (0.00–10.00).
   * Persists multi-table transactional records across `users` and `students` tables with automatic rollback on error.
   * View full academic details and perform in-place profile updates with real-time validation.
2. **User Authentication & Session Management (Akanshi):**
   * Role-based login supporting both `STUDENT` and `ADMIN` credentials.
   * Session tracking and logout via `AuthService`.
3. **Internship Opportunities Management (Navdeep Randhawa):**
   * Browse open internship positions posted by industry partners with stipend, duration, and deadline.
   * Polymorphic opportunity details display (`Opportunity.displayDetails()`).
   * Dynamic eligibility filtering based on minimum CGPA cutoff, minimum semester, and allowed academic branches.
4. **Application Submission & Duplicate Prevention (Navdeep Randhawa):**
   * Submit internship applications with business constraint validation.
   * Custom exception enforcement: `DuplicateApplicationException` prevents re-applying, while `IneligibleStudentException` blocks ineligible applicants.
   * Student dashboard application tracking and placement administrator application oversight.

---

## 2. Architecture & Class Breakdown

The codebase is structured under the `com.skillora` namespace:

```text
com.skillora/
│
├── database/
│   └── DBConnection.java                  # Singleton JDBC connection manager
│
├── model/
│   ├── User.java                          # Abstract base entity for authentication [Akanshi]
│   ├── Student.java                       # Child class extending User with academic fields [Akanshi]
│   ├── Admin.java                         # Child class extending User for placement staff [Akanshi]
│   ├── Opportunity.java                   # Abstract base class for postings with displayDetails() [Navdeep]
│   ├── Internship.java                    # Subclass of Opportunity with eligibility & stipend [Navdeep]
│   └── Application.java                   # Student internship application entity [Navdeep]
│
├── dao/
│   ├── StudentDAO.java                    # DAO interface defining user/student operations [Akanshi]
│   ├── StudentDAOImpl.java                # JDBC implementation with prepared statements [Akanshi]
│   ├── InternshipDAO.java                 # DAO interface for internships & applications [Navdeep]
│   └── InternshipDAOImpl.java             # JDBC implementation for internship operations [Navdeep]
│
├── service/
│   ├── AuthService.java                   # Authentication, registration & session logic [Akanshi]
│   └── InternshipService.java             # Eligibility checks, applications & business rules [Navdeep]
│
├── exception/
│   ├── SkilloraException.java             # Checked base exception [Akanshi]
│   ├── AuthenticationException.java       # Credential failure exception [Akanshi]
│   ├── DuplicateApplicationException.java # Duplicate application prevention exception [Navdeep]
│   └── IneligibleStudentException.java    # Academic criteria mismatch exception [Navdeep]
│
├── utility/
│   ├── ConsoleTheme.java                  # ANSI terminal styling, banners, and alerts
│   ├── InputValidator.java                # Regex validation for email, CGPA, semester
│   └── FileUtil.java                      # External properties configuration loader
│
└── main/
    └── SkilloraApp.java                   # Interactive CLI application & routing entry point
```

---

## 3. Core OOP & Java Concepts Demonstrated

| Concept | Implementation in Code | Contributor |
| :--- | :--- | :--- |
| **Abstraction** | Abstract class `User` declares `getDetails()`; abstract class `Opportunity` declares `displayDetails()`. | Akanshi / Navdeep |
| **Inheritance & `super`** | `Student` & `Admin` extend `User`; `Internship` extends `Opportunity` invoking `super(...)` constructors. | Akanshi / Navdeep |
| **Polymorphism** | Dynamic method dispatch on `getDetails()` and `displayDetails()`; role routing in `SkilloraApp`. | Akanshi / Navdeep |
| **Encapsulation** | Private instance variables with strictly typed public getters, setters, and validation guards across all models. | Akanshi / Navdeep |
| **Custom Exceptions** | `AuthenticationException`, `DuplicateApplicationException`, `IneligibleStudentException` extending `SkilloraException`. | Akanshi / Navdeep |
| **Design Patterns** | **Singleton Pattern** in `DBConnection` ensuring a synchronized database connection pool. | Akanshi |
| **JDBC Architecture** | Separation of DAO interfaces and DAO implementations using `PreparedStatement` and SQL transactions. | Akanshi / Navdeep |

---

## 4. Database Setup (MySQL)

### Step 1: Import Database Schema & Seed Data
Execute the provided SQL script in MySQL Workbench or via terminal:
```bash
mysql -u root -p < db/skillora_schema.sql
```

This creates the `skillora_db` database and sets up:
* `users`: Primary entity table (`user_id`, `name`, `email`, `password`, `role`, `created_at`)
* `students`: Academic attributes linked via `student_id` foreign key
* `admins`: Placement staff attributes linked via `admin_id` foreign key
* `companies`: Partner organizations offering internship drives
* `internships`: Internship listings with CGPA, semester, branch, and stipend attributes
* `applications`: Application records with student-internship unique constraints

### Step 2: Configure Database Credentials
Edit `resources/db.properties` with your local MySQL credentials:
```properties
db.url=jdbc:mysql://localhost:3306/skillora_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=YOUR_MYSQL_PASSWORD
```

---

## 5. Build and Execution

Make the scripts executable (if needed) and run:

```bash
chmod +x build.sh run.sh
```

### Compile:
```bash
./build.sh
```

### Launch Application:
```bash
./run.sh
```

---

## 6. Pre-seeded Demo Credentials

For viva demonstrations and testing:

| Role | Email | Password | Details |
| :--- | :--- | :--- | :--- |
| **Student (Akanshi)** | `akanshi@skillora.edu` | `pass123` | Akanshi Sharma, Roll: 23CS101, Sem 5, CGPA 8.75 |
| **Student (Navdeep)** | `navdeep@skillora.edu` | `pass123` | Navdeep Singh, Roll: 23CS109, Sem 5, CGPA 8.50 |
| **Admin** | `admin@skillora.edu` | `admin123` | Dr. Sharma, Training & Placement Cell, Staff ID: TPO-001 |

---

## 7. Contributors

* **Akanshi** (GitHub: `akanshi2750beai25-cell`) – Student Registration, Authentication & Profile Management
* **Navdeep Randhawa** (GitHub: `NAVDEEPRANDHAWA`) – Internship Opportunities, Eligibility Engine & Application Tracking
* **Course:** Programming in Java (25CSE0202)
