# Spring Security Lab Project

This project demonstrates implementation of security features in a Spring Boot application.

## Project Overview

Simple blog application with secure user authentication and data management. Users can create accounts, write blog posts, and interact through comments while maintaining data security.

## Core Components

### Security Implementation
- Spring Security configuration
- JWT token authentication
- Password encryption (BCrypt)
- Role-based access control

### Database Structure
- Users table with encrypted passwords
- Blog posts with user references
- Comments linked to posts and users
- Category management system

### Main Endpoints

Authentication:
```
/api/auth/signup  [POST] - Create new account
/api/auth/signin  [POST] - Get authentication token
```

Content Management:
```
/api/blogs    [GET, POST] - View/Create blog posts
/api/blogs/id [GET, PUT]  - Manage specific post
/api/comments [POST]      - Add comments
```

## Running the Project

1. Build:
```
mvn clean install
```

2. Start application:
```
mvn spring-boot:run
```

3. Access API at `http://localhost:8080`

## Implementation Notes

- H2 in-memory database for development
- Flyway for database migrations
- Input validation on all endpoints
- Cross-origin resource sharing (CORS) enabled
