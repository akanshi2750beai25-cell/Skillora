# Skillora – Career, Internship & Student Networking Platform

A console-based Java application built using **Core Java OOP** and **MySQL (JDBC)**, designed for the official university evaluation of course **Programming in Java (25CSE0202)**.

---

## 1. Project Overview & Phase 1 Scope

Skillora bridges the gap between students, technical skills, campus placement drives, internship opportunities, and inter-college hackathons.

### Phase 1 Features:
* **Student Registration & Login:** Create and authenticate student accounts with email/password validation.
* **Skill Management:** Catalog of technical skills with proficiency ratings (*Beginner*, *Intermediate*, *Advanced*).
* **Internship Opportunities:** View and filter opportunities based on CGPA cutoffs, eligible branches, and semester.
* **Application System:** Submit internship applications with duplicate prevention.
* **Company Placement Visits:** Directory of visiting companies, dates, and drive formats (*On-Campus*, *Pool-Campus*, *Virtual*).
* **Hackathon Module:** Discover competitions and filter by technical domain or location.
* **Student Networking:** Search peers by technical skill to form hackathon teams.
* **Placement Officer / Admin:** Register companies, post internships, schedule visits, and export applicant reports to a text file.

---

## 2. Project Architecture & 35 Java Classes

Organized across 7 clean packages:

```text
com.skillora/
│
├── database/
│   └── DBConnection.java                 # Singleton JDBC connection manager
│
├── model/
│   ├── User.java                         # Abstract base class for authentication
│   ├── Student.java                      # Extends User with academic fields
│   ├── Admin.java                        # Extends User for placement staff
│   ├── Skill.java                        # Skill entity with proficiency
│   ├── Company.java                      # Company partner entity
│   ├── CompanyVisit.java                 # Campus placement visit entity
│   ├── Opportunity.java                  # Abstract base class for postings
│   ├── Internship.java                   # Extends Opportunity with eligibility
│   ├── Application.java                  # Application tracking entity
│   └── Hackathon.java                    # Competition directory entity
│
├── dao/
│   ├── StudentDAO.java & Impl            # Student CRUD and authentication
│   ├── SkillDAO.java & Impl              # Skills catalog and student mappings
│   ├── CompanyDAO.java & Impl            # Companies and campus visits
│   ├── InternshipDAO.java & Impl         # Internships and applications
│   └── HackathonDAO.java & Impl          # Hackathon listings and filters
│
├── service/
│   ├── AuthService.java                  # Login, registration, session management
│   ├── StudentService.java               # Skill management & peer networking
│   ├── InternshipService.java            # Eligibility engine & application logic
│   └── PlacementService.java             # Admin workflows & file reports
│
├── exception/
│   ├── SkilloraException.java            # Base checked domain exception
│   ├── AuthenticationException.java      # Credential failure
│   ├── DuplicateApplicationException.java# Duplicate application prevention
│   └── IneligibleStudentException.java   # CGPA/branch criteria mismatch
│
├── utility/
│   ├── ConsoleTheme.java                 # ANSI formatting, colors, and banners
│   ├── InputValidator.java               # Regex email, password, and CGPA validation
│   └── FileUtil.java                     # db.properties loader & report exporter
│
└── main/
    ├── SkilloraApp.java                  # Application entry point & router
    ├── StudentMenu.java                  # Student interactive dashboard
    └── AdminMenu.java                    # Admin interactive dashboard
```

---

## 3. Team Responsibilities

| Member | Assigned Modules | Owned Core Classes | Key Viva Focus |
| :--- | :--- | :--- | :--- |
| **Akanshi** | Student Registration, Auth & Profile | `User`, `Student`, `Admin`, `StudentDAO`, `AuthService`, `SkilloraApp` | Abstraction, Inheritance, `super` constructor, Session state |
| **Mridul** | Skills Management & Peer Networking | `Skill`, `SkillDAO`, `StudentService`, `ConsoleTheme`, `StudentMenu` | Collections (`ArrayList`), Method Overloading, ANSI UI |
| **Navdeep**| Internship Opportunities & Applications | `Opportunity`, `Internship`, `Application`, `InternshipDAO`, `InternshipService` | Polymorphism (`displayDetails`), Custom Exceptions, Validation |
| **Paras**  | Company Placement Visits & Admin Panel | `Company`, `CompanyVisit`, `Hackathon`, `CompanyDAO`, `HackathonDAO`, `PlacementService`, `FileUtil`, `AdminMenu` | Interfaces, File I/O (`db.properties`), Date filtering |

---

## 4. Setup and Execution

### Step 1: Database Setup
Import the pre-configured schema and seed data into MySQL:
```bash
mysql -u root -p < db/skillora_schema.sql
```

### Step 2: Configure Credentials
Edit [resources/db.properties](file:///Volumes/college/Skillora/resources/db.properties) if your local MySQL password differs:
```properties
db.url=jdbc:mysql://localhost:3306/skillora_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=root
```

### Step 3: Build & Run
Compile all 35 classes:
```bash
./build.sh
```

Launch the application:
```bash
./run.sh
```

---

## 5. Demo Credentials (from Seed Data)
* **Student Login:** `akanshi@skillora.edu` | Password: `pass123`
* **Admin Login:** `admin@skillora.edu` | Password: `admin123`
