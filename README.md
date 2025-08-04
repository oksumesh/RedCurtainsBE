# RedCurtains Backend

A Spring Boot backend application for the RedCurtains cinema application, built with Kotlin and MySQL.

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Docker and Docker Compose
- Gradle

### 1. Start MySQL Database

```bash
# Start MySQL and phpMyAdmin
docker-compose up -d

# Check if services are running
docker-compose ps
```

### 2. Build and Run the Application

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

### 3. Access the Application

- **Backend API**: http://localhost:8080
- **phpMyAdmin**: http://localhost:8081
  - Username: `myuser`
  - Password: `mypassword`

## 📊 Database Configuration

### MySQL Connection Details
- **Host**: localhost
- **Port**: 3307 (to avoid conflict with existing MySQL)
- **Database**: mydb
- **Username**: myuser
- **Password**: mypassword

### Database Schema
The application uses JPA/Hibernate with automatic schema generation (`spring.jpa.hibernate.ddl-auto=update`).

## 🔧 API Endpoints

### User Management
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/email/{email}` - Get user by email
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `POST /api/users/{id}/verify-email` - Verify user email
- `POST /api/users/{id}/deactivate` - Deactivate user

### Loyalty System
- `GET /api/users/loyalty-tier/{tier}` - Get users by loyalty tier
- `POST /api/users/{id}/loyalty-points` - Add loyalty points

### Utility
- `GET /api/users/active` - Get active users
- `GET /api/users/exists/{email}` - Check if user exists
- `GET /api/users/health` - Health check

## 📝 Sample API Requests

### Create User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

### Add Loyalty Points
```bash
curl -X POST http://localhost:8080/api/users/1/loyalty-points \
  -H "Content-Type: application/json" \
  -d '{
    "points": 100
  }'
```

### Get Users by Loyalty Tier
```bash
curl http://localhost:8080/api/users/loyalty-tier/GOLD
```

## 🏗️ Project Structure

```
src/main/kotlin/com/example/demo/
├── DemoApplication.kt          # Main application class
├── HelloController.kt          # Basic health check controller
├── config/
│   └── SecurityConfig.kt       # Security configuration
├── controller/
│   └── UserController.kt       # User REST endpoints
├── entity/
│   └── User.kt                 # User entity
├── repository/
│   └── UserRepository.kt       # Data access layer
└── service/
    └── UserService.kt          # Business logic
```

## 🔐 Security Features

- **Password Encryption**: BCrypt password hashing
- **CSRF Protection**: Disabled for API endpoints
- **Input Validation**: Request DTOs with validation
- **Error Handling**: Proper HTTP status codes

## 🎯 Features

### User Management
- User registration and authentication
- Email verification system
- User profile management
- Account deactivation

### Loyalty System
- Points-based loyalty program
- Automatic tier calculation
- Tier-based user categorization
- Points tracking and management

### Database Features
- Automatic schema generation
- Optimized indexes for performance
- Sample data initialization
- Connection pooling

## 🛠️ Development

### Running Tests
```bash
./gradlew test
```

### Building for Production
```bash
./gradlew build -x test
```

### Database Migration
The application uses Hibernate's automatic schema generation. For production, consider using Flyway or Liquibase for database migrations.

## 📦 Docker Support

### Using Docker Compose
```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

### Manual Docker Setup
```bash
# Build the application
./gradlew build

# Run with Docker
docker run -p 8080:8080 redcurtains-backend
```

## 🔍 Monitoring

### Health Check
- Endpoint: `GET /api/users/health`
- Returns service status and uptime

### Database Monitoring
- phpMyAdmin available at http://localhost:8081
- MySQL logs: `docker-compose logs mysql`

## 🚨 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Ensure Docker is running
   - Check if MySQL container is up: `docker-compose ps`
   - Verify credentials in `application.properties`

2. **Port Already in Use**
   - Change ports in `docker-compose.yml`
   - Update `application.properties` accordingly

3. **Build Failures**
   - Clean and rebuild: `./gradlew clean build`
   - Check Java version: `java -version`

## 📄 License

This project is part of the RedCurtains cinema application.
