# Skillora – Career, Placement & Student Networking Platform

A modular console application built using **Core Java OOP** and **MySQL (JDBC)**, created for the university evaluation of course **Programming in Java (25CSE0202)**.

This repository integrates:
* **Akanshi's Contribution (`akanshi2750beai25-cell`):** User Authentication, Student Registration & Profile Management
* **Paras Jain's Contribution (`parasjain9807`):** Campus Placement Drives, Partner Companies Directory, Inter-College Hackathons & Placement Officer Dashboard

---

## 1. Integrated Modules & Key Features

### Module A: Student Registration, Authentication & Profile (Akanshi)
* **Role-Based Authentication:** Unified credential verification for `STUDENT` and `ADMIN` accounts with session lifecycle tracking.
* **Student Registration:** Multi-table atomic persistence across `users` and `students` tables with regex and numeric range validations.
* **Student Academic Profile:** View full academic history (Roll Number, College, Branch, Semester, CGPA) and in-place profile updates with real-time validation.

### Module B: Company Placement Drives, Hackathons & Admin Panel (Paras Jain)
* **Partner Companies Management:** Directory of visiting companies with industry classification, corporate website, and recruitment contacts.
* **Campus Placement Calendar:** Schedule and track upcoming campus recruitment drives (*On-Campus*, *Pool-Campus*, *Virtual*) with eligibility branch criteria and date tracking.
* **Inter-College Hackathons Directory:** Explore and publish coding competitions, hackathons, team limits, and registration deadlines with domain and location filters.
* **Placement Officer Dashboard (`AdminMenu`):** Dedicated administrative console for placement cell staff to register companies, schedule drives, publish hackathons, and inspect admin details.
* **File Persistence & Reporting (`FileUtil`):** Export clean, formatted text audit reports of scheduled placement drives and companies to disk.

---

## 2. Project Architecture & Package Structure

The codebase is organized cleanly under the `com.skillora` namespace:

```text
com.skillora/
│
├── database/
│   └── DBConnection.java            # Singleton JDBC connection manager
│
├── model/
│   ├── User.java                    # Abstract base entity for authentication [Akanshi]
│   ├── Student.java                 # Child class extending User with academic fields [Akanshi]
│   ├── Admin.java                   # Child class extending User for placement staff [Akanshi]
│   ├── Company.java                 # Recruiting corporate partner entity [Paras Jain]
│   ├── CompanyVisit.java            # Campus placement drive schedule entity [Paras Jain]
│   └── Hackathon.java               # Inter-college competition entity [Paras Jain]
│
├── dao/
│   ├── StudentDAO.java & Impl       # User authentication & Student CRUD [Akanshi]
│   ├── CompanyDAO.java & Impl       # Companies & placement visits persistence [Paras Jain]
│   └── HackathonDAO.java & Impl     # Hackathons directory persistence & filters [Paras Jain]
│
├── service/
│   ├── AuthService.java             # Authentication & session state [Akanshi]
│   └── PlacementService.java        # Placement drives, hackathons & file export [Paras Jain]
│
├── exception/
│   ├── SkilloraException.java       # Base domain checked exception [Akanshi]
│   └── AuthenticationException.java # Specialized credential failure exception [Akanshi]
│
├── utility/
│   ├── ConsoleTheme.java            # ANSI terminal styling, banners, and status alerts
│   ├── InputValidator.java          # Regex validation for email, CGPA, semester [Akanshi]
│   └── FileUtil.java                # db.properties loader [Akanshi] & report writer [Paras Jain]
│
└── main/
    ├── SkilloraApp.java             # Application entry point & router [Akanshi + Paras Jain]
    └── AdminMenu.java               # Placement Officer interactive dashboard [Paras Jain]
```

---

## 3. Team Responsibilities

| Contributor | GitHub Username | Assigned Modules | Owned Core Classes | Key Concepts Demonstrated |
| :--- | :--- | :--- | :--- | :--- |
| **Akanshi** | `akanshi2750beai25-cell` | Student Registration, Auth & Profile Management | `User`, `Student`, `Admin`, `StudentDAO`, `AuthService`, `SkilloraApp` | Abstraction, Inheritance (`super`), JDBC Transactions, Session Management |
| **Paras Jain** | `parasjain9807` | Company Placement Drives, Hackathons & Admin Panel | `Company`, `CompanyVisit`, `Hackathon`, `CompanyDAO`, `HackathonDAO`, `PlacementService`, `FileUtil`, `AdminMenu` | Interfaces, DAO Pattern, File I/O (`BufferedWriter`/`BufferedReader`), Date Filtering |

---

## 4. Database Setup (MySQL)

### Step 1: Import Database Schema & Seed Data
Execute the provided SQL script in MySQL terminal or MySQL Workbench:
```bash
mysql -u root -p < db/skillora_schema.sql
```

This sets up the `skillora_db` database with tables:
* `users` & `students` (Student identity & academic records)
* `admins` (Placement cell staff records)
* `companies` (Corporate recruitment partners)
* `company_visits` (Campus recruitment calendar)
* `hackathons` (Competitions directory)

### Step 2: Configure Database Credentials
Edit `resources/db.properties` with your local MySQL credentials:
```properties
db.url=jdbc:mysql://localhost:3306/skillora_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=YOUR_MYSQL_PASSWORD
```

---

## 5. Build and Execution

### Build
Compile the Java sources into the `bin/` directory:
```bash
bash build.sh
```

### Run
Launch the interactive console application:
```bash
bash run.sh
```

### Demo Accounts for Viva Evaluation
* **Placement Officer / Admin:**
  * Email: `admin@skillora.edu`
  * Password: `admin123`
* **Student Accounts:**
  * `akanshi@skillora.edu` / `pass123` (Akanshi Sharma - CS Sem 5)
  * `paras@skillora.edu` / `pass123` (Paras Malhotra - IT Sem 5)
