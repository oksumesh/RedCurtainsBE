-- RedCurtains Database Initialization Script

-- Use the database
USE mydb;

-- Create users table (will be created by JPA, but adding for reference)
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(20),
    is_active BOOLEAN DEFAULT TRUE,
    email_verified BOOLEAN DEFAULT FALSE,
    created_at DATETIME NOT NULL,
    updated_at DATETIME,
    last_login_at DATETIME,
    loyalty_points INT DEFAULT 0,
    loyalty_tier VARCHAR(20) DEFAULT 'BRONZE'
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(is_active);
CREATE INDEX IF NOT EXISTS idx_users_loyalty_tier ON users(loyalty_tier);
CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);

-- Insert sample data (optional)
INSERT INTO users (email, password, first_name, last_name, is_active, email_verified, created_at, loyalty_points, loyalty_tier) 
VALUES 
('admin@redcurtains.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin', 'User', true, true, NOW(), 5000, 'GOLD'),
('john.doe@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'John', 'Doe', true, true, NOW(), 1200, 'SILVER'),
('jane.smith@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Jane', 'Smith', true, false, NOW(), 500, 'BRONZE')
ON DUPLICATE KEY UPDATE email = email;

-- Show the created table
DESCRIBE users;

-- Show sample data
SELECT id, email, first_name, last_name, loyalty_points, loyalty_tier, created_at FROM users; 