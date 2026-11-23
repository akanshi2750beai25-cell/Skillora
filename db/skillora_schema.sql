-- ==============================================================================
-- Skillora Database Schema & Seed Data
-- Integrated Modules:
-- 1. Student Registration, Authentication & Profile Management (Akanshi)
-- 2. Company Placement Drives, Campus Visits & Hackathons (Paras Jain)
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

-- 4. Companies Table (Contributed by Paras Jain)
CREATE TABLE IF NOT EXISTS companies (
    company_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(150) NOT NULL UNIQUE,
    industry VARCHAR(100) NOT NULL,
    website VARCHAR(150),
    contact_email VARCHAR(100)
);

-- 5. Company Visits Table - Placement Calendar (Contributed by Paras Jain)
CREATE TABLE IF NOT EXISTS company_visits (
    visit_id INT AUTO_INCREMENT PRIMARY KEY,
    company_id INT NOT NULL,
    college_name VARCHAR(150) NOT NULL,
    visit_date DATE NOT NULL,
    drive_type ENUM('On-Campus', 'Pool-Campus', 'Virtual') DEFAULT 'On-Campus',
    eligible_branches VARCHAR(255) NOT NULL,
    FOREIGN KEY (company_id) REFERENCES companies(company_id) ON DELETE CASCADE
);

-- 6. Hackathons Table - Competitions Directory (Contributed by Paras Jain)
CREATE TABLE IF NOT EXISTS hackathons (
    hackathon_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    host_college VARCHAR(150) NOT NULL,
    event_date DATE NOT NULL,
    domain VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    team_size_limit INT DEFAULT 4,
    registration_deadline DATE NOT NULL
);

-- ==============================================================================
-- Seed Data for Evaluation & Testing
-- ==============================================================================

-- Default Administrator
INSERT IGNORE INTO users (user_id, name, email, password, role) 
VALUES (1, 'Dr. Sharma (Placement Officer)', 'admin@skillora.edu', 'admin123', 'ADMIN');

INSERT IGNORE INTO admins (admin_id, department, staff_id) 
VALUES (1, 'Training & Placement Cell', 'TPO-001');

-- Default Seed Students
INSERT IGNORE INTO users (user_id, name, email, password, role) VALUES 
(2, 'Akanshi Sharma', 'akanshi@skillora.edu', 'pass123', 'STUDENT'),
(5, 'Paras Malhotra', 'paras@skillora.edu', 'pass123', 'STUDENT');

INSERT IGNORE INTO students (student_id, roll_number, college, branch, semester, cgpa) VALUES 
(2, '23CS101', 'Chitkara University', 'Computer Science', 5, 8.75),
(5, '23IT205', 'Chitkara University', 'Information Technology', 5, 7.90);

-- Sample Companies (Contributed by Paras Jain)
INSERT IGNORE INTO companies (company_id, company_name, industry, website, contact_email) VALUES
(1, 'Google India', 'Technology', 'https://careers.google.com', 'campus@google.com'),
(2, 'Infosys', 'IT Services', 'https://infosys.com', 'careers@infosys.com'),
(3, 'Microsoft IDC', 'Software Development', 'https://careers.microsoft.com', 'recruit@microsoft.com');

-- Sample Company Visits (Contributed by Paras Jain)
INSERT IGNORE INTO company_visits (visit_id, company_id, college_name, visit_date, drive_type, eligible_branches) VALUES
(1, 1, 'Chitkara University', '2026-12-05', 'On-Campus', 'Computer Science, Information Technology'),
(2, 2, 'Chitkara University', '2026-12-12', 'On-Campus', 'Computer Science, Information Technology, Electronics'),
(3, 3, 'Chitkara University', '2026-12-20', 'Virtual', 'Computer Science, Information Technology');

-- Sample Hackathons (Contributed by Paras Jain)
INSERT IGNORE INTO hackathons (hackathon_id, title, host_college, event_date, domain, location, team_size_limit, registration_deadline) VALUES
(1, 'Smart India Innovation Hackathon', 'IIT Delhi', '2026-12-18', 'FinTech & Web3', 'New Delhi', 4, '2026-12-10'),
(2, 'HackPulse 2026', 'Chitkara University', '2026-12-22', 'Healthcare & AI', 'Chandigarh', 4, '2026-12-15'),
(3, 'CodeQuest National Challenge', 'BITS Pilani', '2027-01-10', 'EdTech & Smart Cities', 'Pilani', 3, '2026-12-28');
