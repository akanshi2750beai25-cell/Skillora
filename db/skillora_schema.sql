-- ==============================================================================
-- Skillora Database Schema & Seed Data (Akanshi's Contribution)
-- Module: Student Registration, Authentication & Profile Management
-- Course: Programming in Java (25CSE0202)
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS skillora_db;
USE skillora_db;

-- 1. Users Table (Base entity for authentication & role identification)
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT', 'ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Students Table (Child entity extending Users with academic attributes)
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY,
    roll_number VARCHAR(50) NOT NULL UNIQUE,
    college VARCHAR(150) NOT NULL,
    branch VARCHAR(100) NOT NULL,
    semester INT NOT NULL,
    cgpa DECIMAL(3,2) NOT NULL,
    FOREIGN KEY (student_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 3. Admins Table (Child entity extending Users for college placement staff)
CREATE TABLE IF NOT EXISTS admins (
    admin_id INT PRIMARY KEY,
    department VARCHAR(100) NOT NULL,
    staff_id VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ==============================================================================
-- Navdeep Randhawa's Contribution: Internship Opportunities & Applications
-- ==============================================================================

-- 4. Companies Table (Partner organizations offering internships)
CREATE TABLE IF NOT EXISTS companies (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL UNIQUE,
    industry VARCHAR(100) NOT NULL,
    website VARCHAR(150),
    contact_email VARCHAR(100)
);

-- 5. Internships Table (Opportunities with eligibility gates)
CREATE TABLE IF NOT EXISTS internships (
    internship_id INT AUTO_INCREMENT PRIMARY KEY,
    company_id INT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    stipend DECIMAL(10,2) NOT NULL,
    duration_months INT NOT NULL,
    location VARCHAR(100) NOT NULL,
    min_cgpa DECIMAL(3,2) DEFAULT 0.00,
    eligible_branches VARCHAR(255) NOT NULL,
    min_semester INT DEFAULT 1,
    required_skills VARCHAR(255),
    deadline DATE NOT NULL,
    status ENUM('OPEN', 'CLOSED') DEFAULT 'OPEN',
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE
);

-- 6. Applications Table (Student applications with duplicate prevention)
CREATE TABLE IF NOT EXISTS applications (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    internship_id INT NOT NULL,
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('APPLIED', 'SHORTLISTED', 'REJECTED') DEFAULT 'APPLIED',
    remarks VARCHAR(255),
    UNIQUE KEY unique_application (student_id, internship_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (internship_id) REFERENCES internships(internship_id) ON DELETE CASCADE
);

-- ==============================================================================
-- Seed Data for Evaluation & Testing
-- ==============================================================================

-- Default Administrator
INSERT IGNORE INTO users (user_id, name, email, password, role) 
VALUES (1, 'Dr. Sharma (Placement Officer)', 'admin@skillora.edu', 'admin123', 'ADMIN');

INSERT IGNORE INTO admins (admin_id, department, staff_id) 
VALUES (1, 'Training & Placement Cell', 'TPO-001');

-- Default Seed Student (Akanshi)
INSERT IGNORE INTO users (user_id, name, email, password, role) 
VALUES (2, 'Akanshi Sharma', 'akanshi@skillora.edu', 'pass123', 'STUDENT');

INSERT IGNORE INTO students (student_id, roll_number, college, branch, semester, cgpa) 
VALUES (2, '23CS101', 'Chitkara University', 'Computer Science', 5, 8.75);

-- Seed Student (Navdeep)
INSERT IGNORE INTO users (user_id, name, email, password, role) 
VALUES (4, 'Navdeep Singh', 'navdeep@skillora.edu', 'pass123', 'STUDENT');

INSERT IGNORE INTO students (student_id, roll_number, college, branch, semester, cgpa) 
VALUES (4, '23CS109', 'Chitkara University', 'Computer Science', 5, 8.50);

-- Partner Companies for Internships
INSERT IGNORE INTO companies (company_id, company_name, industry, website, contact_email) VALUES
(1, 'Google India', 'Technology', 'https://careers.google.com', 'campus@google.com'),
(2, 'Infosys', 'IT Services', 'https://infosys.com', 'careers@infosys.com'),
(3, 'Microsoft IDC', 'Software Development', 'https://careers.microsoft.com', 'recruit@microsoft.com');

-- Sample Internships (Navdeep's Module)
INSERT IGNORE INTO internships (internship_id, company_id, title, description, stipend, duration_months, location, min_cgpa, eligible_branches, min_semester, required_skills, deadline) VALUES
(101, 1, 'Software Engineer Intern', 'Develop scalable backend microservices and REST APIs.', 50000.00, 6, 'Bangalore', 8.00, 'Computer Science, Information Technology', 5, 'Java, MySQL', '2026-12-25'),
(102, 2, 'Systems Trainee Intern', 'Enterprise application maintenance, debugging, and testing.', 25000.00, 3, 'Chandigarh', 7.00, 'Computer Science, Information Technology, Electronics', 4, 'Java', '2026-12-20'),
(103, 3, 'Cloud Solutions Intern', 'Assist with Azure deployment pipelines and monitoring.', 45000.00, 6, 'Hyderabad', 8.25, 'Computer Science, Information Technology', 5, 'Python, Cloud', '2026-12-30');
