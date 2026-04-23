# Authentication & Inventory Management Service

## 🏷️ Badges
![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-green)
![MySQL](https://img.shields.io/badge/MySQL-Database-orange)
![JWT](https://img.shields.io/badge/Auth-JWT-red)

---

## 📖 Project Description
This project is a backend system built using Spring Boot that provides:
- JWT-based authentication
- Role-based access control (ADMIN, STAFF)
- Inventory management with stock tracking

---

## 🛠️ Tech Stack

| Technology     | Usage                  |
|---------------|----------------------|
| Java 21       | Programming Language |
| Spring Boot   | Backend Framework    |
| MySQL         | Database             |
| Spring Security | Authentication     |
| JWT           | Token-based security |

---

## Features
- User registration & login
- Password encryption using BCrypt
- JWT authentication
- Role-based authorization
- Inventory CRUD operations
- Stock-in / Stock-out management
- Input validation
- Exception handling

---

src/
├── controller
├── service
├── repository
├── model
├── security
├── config


---

## 🚀 Setup Instructions

1. Clone the repository: https://github.com/Rahul792004/auth-inventory-service.git

2. Configure MySQL:
- Create database: `inventory_db`
- Update credentials in `application.properties`

3. Run the application:
- Run `AuthInventoryServiceApplication.java`

4. Server will start at:
   http://localhost:8080


---

## 📡 API Endpoints

| Method | Endpoint | Description | Access |
|-------|---------|------------|--------|
| POST  | /auth/register | Register user | Public |
| POST  | /auth/login | Login & get JWT | Public |
| POST  | /inventory/items | Create item | ADMIN |
| GET   | /inventory/items | Get all items | ADMIN, STAFF |
| POST  | /inventory/items/{id}/stock-in | Add stock | ADMIN |
| POST  | /inventory/items/{id}/stock-out | Remove stock | ADMIN |

---

## 📋 Sample Requests

### Register
```json
{
"email": "admin@gmail.com",
"password": "Admin@123",
"role": "ADMIN"
}




Login Response:
```json
{
  "token": "jwt_token_here"
}




🗄️ Database Schema
User Table
id
email
password
role
Item Table
id
name
quantity


⚠️ Error Handling
Invalid credentials → 401 Unauthorized
Item not found → 404 Not Found
Insufficient stock → 400 Bad Request


🔐 Validation Rules
Email must be valid format
Password must contain:
8+ characters
Uppercase letter
Number
Special character
Quantity must be ≥ 0

👤 Role Permissions
Role	Permissions
ADMIN	Full access (create, update, stock)
STAFF	View inventory only

---
