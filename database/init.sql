-- ============================================================
--  Travel Management System — Database Setup
--  Run this BEFORE starting the Spring Boot application
-- ============================================================

IF DB_ID(N'travel_management_db') IS NULL
BEGIN
        CREATE DATABASE travel_management_db;
END;
GO

USE travel_management_db;
GO

-- Spring Boot / Hibernate will auto-create all tables via
-- spring.jpa.hibernate.ddl-auto=update
-- This script just creates the DB and a dedicated user.

-- Optional: create a dedicated DB user (recommended for production)
-- CREATE USER IF NOT EXISTS 'tms_user'@'localhost' IDENTIFIED BY 'tms_password_123';
-- GRANT ALL PRIVILEGES ON travel_management_db.* TO 'tms_user'@'localhost';
-- FLUSH PRIVILEGES;

-- ============================================================
--  If you prefer manual table creation (ddl-auto=none),
--  uncomment and run the statements below INSTEAD:
-- ============================================================

/*
CREATE TABLE IF NOT EXISTS departments (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL UNIQUE,
    budget_limit  DOUBLE,
    allowed_class VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS users (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    role           ENUM('EMPLOYEE','MANAGER','FINANCE','ADMIN') NOT NULL,
    department_id  BIGINT,
    manager_id     BIGINT,
    created_at     DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (manager_id)    REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS travel_requests (
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    employee_id             BIGINT NOT NULL,
    destination             VARCHAR(200) NOT NULL,
    travel_from             DATE NOT NULL,
    travel_to               DATE NOT NULL,
    purpose                 TEXT NOT NULL,
    estimated_budget        DOUBLE NOT NULL,
    status                  ENUM('DRAFT','SUBMITTED','MANAGER_APPROVED','MANAGER_REJECTED',
                                  'FINANCE_APPROVED','FINANCE_REJECTED','CLOSED') NOT NULL DEFAULT 'DRAFT',
    policy_violated         BOOLEAN DEFAULT FALSE,
    policy_violation_reason TEXT,
    created_at              DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at              DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS approval_steps (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    travel_request_id BIGINT NOT NULL,
    approver_id       BIGINT,
    level             ENUM('MANAGER','FINANCE') NOT NULL,
    action            ENUM('DRAFT','SUBMITTED','MANAGER_APPROVED','MANAGER_REJECTED',
                           'FINANCE_APPROVED','FINANCE_REJECTED','CLOSED'),
    comments          TEXT,
    acted_at          DATETIME,
    created_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id),
    FOREIGN KEY (approver_id)       REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS itinerary_items (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    travel_request_id BIGINT NOT NULL,
    type              VARCHAR(50) NOT NULL,
    details           TEXT NOT NULL,
    start_dt          DATETIME,
    end_dt            DATETIME,
    location          VARCHAR(200),
    FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id)
);

CREATE TABLE IF NOT EXISTS expenses (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    travel_request_id BIGINT NOT NULL,
    category          ENUM('FOOD','STAY','TRANSPORT','MISCELLANEOUS') NOT NULL,
    amount            DOUBLE NOT NULL,
    description       VARCHAR(300),
    proof_path        VARCHAR(500),
    proof_file_name   VARCHAR(200),
    status            VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at        DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (travel_request_id) REFERENCES travel_requests(id)
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    request_id  BIGINT,
    actor_id    BIGINT,
    actor_name  VARCHAR(100),
    action      VARCHAR(100) NOT NULL,
    note        TEXT,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP
);
*/
