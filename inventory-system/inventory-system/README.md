# Inventory Management System

Complete Spring Boot 3 backend with JWT authentication and inventory management.

## Tech Stack
- Java 21, Spring Boot 3.x, MySQL, Maven
- JWT Authentication, BCrypt Password Hashing
- Clean Architecture: Controller → Service → Repository → Model

---

## Quick Start

### 1. Prerequisites
- ✅ Java 21 installed
- ✅ Maven installed  
- ✅ MySQL running on localhost:3306

### 2. Setup Database
**Option A:** Let app auto-create (recommended)
- MySQL will auto-create `inventory_db` database

**Option B:** Create manually
```sql
CREATE DATABASE inventory_db;
```

### 3. Configure MySQL Password
Edit `src/main/resources/application.properties` line 3:
```properties
spring.datasource.password=YOUR_MYSQL_PASSWORD
```
Common passwords: `root`, `1234`, `admin`, or leave empty `=`

### 4. Run Application
```bash
cd d:\inventory-system\inventory-system
mvn spring-boot:run
```

### 5. Wait for Success Message
```
Started InventorySystemApplication in X seconds
```

### 6. Test with Postman
App runs on `http://localhost:8080`

---

## API Endpoints

### Authentication (Public)
| Method | Endpoint      | Description     |
|--------|---------------|-----------------|
| POST   | /auth/register| Register user   |
| POST   | /auth/login   | Login, get JWT  |

### Inventory (Protected)
| Method | Endpoint                        | Access       | Description        |
|--------|---------------------------------|--------------|--------------------|
| POST   | /inventory/items                | ADMIN        | Create item        |
| GET    | /inventory/items                | ADMIN, STAFF | Get all items      |
| POST   | /inventory/items/{id}/stock-in  | ADMIN        | Add stock          |
| POST   | /inventory/items/{id}/stock-out | ADMIN        | Remove stock       |

> **Authorization:** Add header `Authorization: Bearer <token>` for protected endpoints

---

## Complete API Testing Flow

### Step 1: Register Admin User
```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "email": "admin@example.com",
  "password": "Admin@1234",
  "role": "ADMIN"
}
```
**Response:** `201 Created`

### Step 2: Login and Get Token
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "password": "Admin@1234"
}
```
**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0..."
}
```
**→ Copy this token for next requests**

### Step 3: Create Inventory Item
```http
POST http://localhost:8080/inventory/items
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "name": "Laptop",
  "quantity": 10
}
```
**Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "quantity": 10
}
```

### Step 4: Get All Items
```http
GET http://localhost:8080/inventory/items?page=0&size=10
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```
**Response:**
```json
{
  "content": [
    { "id": 1, "name": "Laptop", "quantity": 10 }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 10,
  "number": 0
}
```

### Step 5: Add Stock (Stock-In)
```http
POST http://localhost:8080/inventory/items/1/stock-in
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "quantity": 5
}
```
**Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "quantity": 15
}
```

### Step 6: Remove Stock (Stock-Out)
```http
POST http://localhost:8080/inventory/items/1/stock-out
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "quantity": 3
}
```
**Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "quantity": 12
}
```

### Step 7: Test STAFF Role (Limited Access)
Register a STAFF user:
```json
{
  "email": "staff@example.com",
  "password": "Staff@1234",
  "role": "STAFF"
}
```
- ✅ STAFF can: `GET /inventory/items`
- ❌ STAFF cannot: `POST /inventory/items`, stock-in, stock-out (returns `403 Forbidden`)

---

## Validation Rules

### Registration
- **Email:** Valid format, must be unique
- **Password:** Min 8 chars, 1 uppercase, 1 number, 1 special char (`@$!%*?&`)
- **Role:** Must be `ADMIN` or `STAFF`

### Inventory
- **Item name:** Cannot be blank
- **Create quantity:** Must be ≥ 0
- **Stock-in/out quantity:** Must be > 0
- **Stock-out:** Cannot reduce below 0 (throws error)

---

## Error Handling

### Validation Errors
```json
{
  "password": "Password must be min 8 chars with uppercase, number, and special character",
  "email": "Invalid email format"
}
```

### Business Logic Errors
```json
{
  "error": "Insufficient stock. Available: 5"
}
```

### Authentication Errors
- `401 Unauthorized` → Missing/invalid token
- `403 Forbidden` → Valid token but insufficient role

---

## Database Schema

### Users Table
```sql
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('ADMIN', 'STAFF') NOT NULL
);
```

### Items Table
```sql
CREATE TABLE items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  quantity INT NOT NULL,
  version BIGINT -- for optimistic locking
);
```

### Transaction History Table
```sql
CREATE TABLE transaction_history (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  item_id BIGINT NOT NULL,
  type ENUM('STOCK_IN', 'STOCK_OUT') NOT NULL,
  quantity INT NOT NULL,
  timestamp DATETIME NOT NULL,
  FOREIGN KEY (item_id) REFERENCES items(id)
);
```

---

## Architecture

```
Controller Layer    → REST endpoints, validation
Service Layer       → Business logic, transactions  
Repository Layer    → Data access (JPA)
Model Layer         → Entities (User, Item, TransactionHistory)
Security Layer      → JWT authentication, role-based access
DTO Layer          → Request/Response objects
Exception Layer    → Global error handling
```

---

## Features Implemented

✅ **Authentication**
- User registration with validation
- Login with JWT token generation
- BCrypt password hashing

✅ **Authorization**  
- Role-based access control (ADMIN/STAFF)
- JWT token validation on every request

✅ **Inventory Management**
- Create items (ADMIN only)
- View items (ADMIN + STAFF)
- Stock-in operations (ADMIN only)
- Stock-out operations (ADMIN only)
- Stock validation (no negative quantities)

✅ **Data Integrity**
- Transaction history tracking
- Optimistic locking for concurrent updates
- Database constraints

✅ **Error Handling**
- Global exception handler
- Validation error responses
- Business logic error responses

✅ **Best Practices**
- Clean architecture
- DTO pattern
- Transactional operations
- Pagination support

---

## Troubleshooting

**Problem:** `Access denied for user 'root'`  
**Solution:** Update MySQL password in `application.properties`

**Problem:** `Communications link failure`  
**Solution:** Start MySQL service in Windows Services

**Problem:** `Could not send request` in Postman  
**Solution:** Ensure app shows "Started InventorySystemApplication"

**Problem:** `401 Unauthorized`  
**Solution:** Add `Authorization: Bearer <token>` header

**Problem:** `403 Forbidden`  
**Solution:** STAFF role trying ADMIN-only endpoint

See `TROUBLESHOOTING.md` for detailed solutions.

---

## Project Structure

```
src/main/java/com/inventory_system/
├── InventorySystemApplication.java     # Main class
├── User.java                          # User entity
├── Item.java                          # Item entity  
├── TransactionHistory.java            # Transaction entity
├── Role.java                          # Role enum
├── TransactionType.java               # Transaction type enum
├── UserRepository.java                # User data access
├── ItemRepository.java                # Item data access
├── TransactionHistoryRepository.java  # Transaction data access
├── RegisterRequest.java               # Registration DTO
├── LoginRequest.java                  # Login DTO
├── AuthResponse.java                  # Auth response DTO
├── ItemRequest.java                   # Item creation DTO
├── ItemResponse.java                  # Item response DTO
├── StockRequest.java                  # Stock operation DTO
├── AuthService.java                   # Authentication logic
├── InventoryService.java              # Inventory logic
├── AuthController.java                # Auth endpoints
├── InventoryController.java           # Inventory endpoints
├── JwtUtil.java                       # JWT utilities
├── JwtFilter.java                     # JWT request filter
├── SecurityConfig.java                # Security configuration
├── UserDetailsServiceImpl.java       # User loading for Spring Security
├── GlobalExceptionHandler.java       # Error handling
├── ResourceNotFoundException.java    # Custom exception
└── BadRequestException.java          # Custom exception
```

**Total:** 26 files, fully functional Spring Boot application.