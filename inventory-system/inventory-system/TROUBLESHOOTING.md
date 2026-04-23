# TROUBLESHOOTING GUIDE

## Quick Fix Steps

### Step 1: Check MySQL Service
1. Press `Win + R`, type `services.msc`, press Enter
2. Find "MySQL80" or "MySQL57" in the list
3. If Status is not "Running" → Right-click → Start

### Step 2: Fix MySQL Password
The most common issue is wrong MySQL password. Try these in order:

#### Option A: Password is "root"
- File: `src/main/resources/application.properties`
- Line 3: `spring.datasource.password=root`

#### Option B: No password (empty)
- File: `src/main/resources/application.properties`  
- Line 3: `spring.datasource.password=`

#### Option C: Password is "1234"
- File: `src/main/resources/application.properties`
- Line 3: `spring.datasource.password=1234`

### Step 3: Run the Application
```
cd d:\inventory-system\inventory-system
mvn spring-boot:run
```

---

## Common Errors & Solutions

### Error: "Access denied for user 'root'@'localhost'"
**Cause:** Wrong MySQL password
**Fix:** Update `spring.datasource.password=` in `application.properties`

### Error: "Communications link failure"
**Cause:** MySQL is not running
**Fix:** Start MySQL service in Windows Services

### Error: "Unknown database 'inventory_db'"
**Cause:** Database doesn't exist (should auto-create)
**Fix:** The URL has `createDatabaseIfNotExist=true` so this should not happen

### Error: "Port 8080 already in use"
**Cause:** Another application using port 8080
**Fix:** Add `server.port=8081` to `application.properties`

### Error: "Could not send request" in Postman
**Cause:** Application is not running
**Fix:** Make sure you see "Started InventorySystemApplication" in terminal

---

## Test MySQL Connection Manually

Open Command Prompt and try:
```
mysql -u root -p
```
Enter your password. If it connects, use that same password in `application.properties`.

---

## Alternative: Use H2 Database (No MySQL needed)

If MySQL keeps failing, replace `application.properties` with:
```properties
# H2 Database (in-memory, no setup needed)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.h2.console.enabled=true

# JWT
jwt.secret=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
jwt.expiration=86400000

server.port=8080
```

Then add H2 dependency to `pom.xml`:
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

---

## Success Indicators

When everything works, you'll see:
```
Started InventorySystemApplication in X seconds
```

Then Postman requests will work.