# Secure Blog API

**Developer:** Ahmet Gunes  
**Student ID:** 487833

A secure blog API developed with Spring Boot.

## Features
- 🔐 User Authentication and Authorization
- 📝 Blog Post Management
- 🏷️ Category System
- 💬 Comment System
- 🛡️ Role-based Access Control

## Setup & Installation
1. JDK 17 required
2. MySQL database must be running
3. Configure database settings in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/blog_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```
4. Build project: `mvn clean install`
5. Run application: `./mvnw spring-boot:run`

## Quick Start Guide
1. **Register User**
   ```bash
   curl -X POST http://localhost:8080/api/auth/register 
   -H "Content-Type: application/json" 
   -d '{"username":"user1", "password":"strong-password", "email":"user@example.com"}'
   ```

2. **Login**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login 
   -H "Content-Type: application/json" 
   -d '{"username":"user1", "password":"strong-password"}'
   ```
   
3. **Create Blog Post**
   ```bash
   curl -X POST http://localhost:8080/api/posts 
   -H "Authorization: Bearer TOKEN" 
   -H "Content-Type: application/json" 
   -d '{"title":"My First Post", "content":"Hello World!"}'
   ```

## API Reference

### Authentication
```
POST /api/auth/register  - Register new user
POST /api/auth/login    - Login user
```

### Blog & Comments
```
GET/POST    /api/posts          - List/Create posts
GET/POST    /api/posts/{id}/comments  - List/Add comments
```

### Categories (Admin)
```
GET/POST    /api/categories     - List/Create categories
```

## Security Features
- BCrypt password encryption
- JWT token authentication
- XSS & CSRF protection
- Role-based authorization
