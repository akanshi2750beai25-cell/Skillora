# Skillora – Student Registration, Authentication & Profile System

A modular, production-grade console application built using **Core Java OOP** and **MySQL (JDBC)**, created for the university evaluation of course **Programming in Java (25CSE0202)**.

This repository represents **Akanshi's standalone contribution** to the Skillora platform, focusing on:
* **User Authentication & Session Management**
* **Student Registration with Input Validation**
* **Student Profile View & Update Workflows**
* **Role-Based User Architecture (Student & Placement Admin)**
* **Secure Database Persistence with JDBC & Prepared Statements**

---

## 1. Module Overview & Features

### Core Capabilities:
1. **Student Registration:**
   * Validates name, university roll number, email address, password strength, academic branch, semester (1–12), and CGPA (0.00–10.00).
   * Persists multi-table transactional records across `users` and `students` tables with automatic rollback on error.
2. **User Authentication:**
   * Role-based login supporting both `STUDENT` and `ADMIN` credentials.
   * Session tracking and logout via `AuthService`.
3. **Student Profile Management:**
   * View full academic details (Roll Number, College, Branch, Semester, CGPA).
   * In-place update of profile details with real-time validation and database synchronization.
4. **Placement Administrator Profile:**
   * Authenticate placement staff accounts, viewing department and staff identifier.

---

## 2. Architecture & Class Breakdown

The codebase is structured under the `com.skillora` namespace:

```text
com.skillora/
│
├── database/
│   └── DBConnection.java            # Singleton JDBC connection manager
│
├── model/
│   ├── User.java                    # Abstract base entity for authentication
│   ├── Student.java                 # Child class extending User with academic fields
│   └── Admin.java                   # Child class extending User for placement staff
│
├── dao/
│   ├── StudentDAO.java              # DAO interface defining user operations
│   └── StudentDAOImpl.java          # JDBC implementation with prepared statements
│
├── service/
│   └── AuthService.java             # Authentication, registration & session logic
│
├── exception/
│   ├── SkilloraException.java       # Checked base exception
│   └── AuthenticationException.java # Specialized credential failure exception
│
├── utility/
│   ├── ConsoleTheme.java            # ANSI terminal styling, banners, and alerts
│   ├── InputValidator.java          # Regex validation for email, CGPA, semester
│   └── FileUtil.java                # External properties configuration loader
│
└── main/
    └── SkilloraApp.java             # Interactive CLI application & routing entry point
```

---

## 3. Core OOP & Java Concepts Demonstrated

| Concept | Implementation in Code |
| :--- | :--- |
| **Abstraction** | Abstract class `User` declares the abstract method `getDetails()`, forcing subclasses to provide role-specific formatting. |
| **Inheritance & `super`** | `Student` and `Admin` extend `User`, invoking `super(...)` constructors to initialize base authentication attributes. |
| **Encapsulation** | Private and protected instance variables with strictly typed public getters, setters, and validation guards. |
| **Polymorphism** | Dynamic method dispatch on `getDetails()`; role casting and `instanceof` routing in `SkilloraApp`. |
| **Design Patterns** | **Singleton Pattern** in `DBConnection` ensuring a single, synchronized database connection pool. |
| **Custom Exceptions** | `AuthenticationException` extending `SkilloraException` for clean domain-specific error handling. |
| **JDBC Transactions** | `conn.setAutoCommit(false)` with atomic commit and rollback in `StudentDAOImpl.registerStudent()`. |

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
| **Student** | `akanshi@skillora.edu` | `pass123` | Akanshi Sharma, Roll: 23CS101, Sem 5, CGPA 8.75 |
| **Admin** | `admin@skillora.edu` | `admin123` | Dr. Sharma, Training & Placement Cell, Staff ID: TPO-001 |

---

## 7. Author

* **Akanshi**
* **Course:** Programming in Java (25CSE0202)
