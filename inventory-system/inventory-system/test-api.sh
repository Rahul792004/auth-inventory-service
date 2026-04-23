#!/bin/bash
# API Test Script - Run this after starting the application

BASE_URL="http://localhost:8080"

echo "=== Testing Inventory Management System APIs ==="
echo

# Test 1: Register Admin
echo "1. Registering admin user..."
curl -X POST "$BASE_URL/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@test.com",
    "password": "Admin@1234",
    "role": "ADMIN"
  }' \
  -w "\nStatus: %{http_code}\n\n"

# Test 2: Login and get token
echo "2. Logging in..."
TOKEN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@test.com",
    "password": "Admin@1234"
  }')

TOKEN=$(echo $TOKEN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "Token received: ${TOKEN:0:50}..."
echo

# Test 3: Create item
echo "3. Creating inventory item..."
curl -X POST "$BASE_URL/inventory/items" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Test Laptop",
    "quantity": 10
  }' \
  -w "\nStatus: %{http_code}\n\n"

# Test 4: Get all items
echo "4. Getting all items..."
curl -X GET "$BASE_URL/inventory/items?page=0&size=10" \
  -H "Authorization: Bearer $TOKEN" \
  -w "\nStatus: %{http_code}\n\n"

# Test 5: Stock in
echo "5. Adding stock..."
curl -X POST "$BASE_URL/inventory/items/1/stock-in" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "quantity": 5
  }' \
  -w "\nStatus: %{http_code}\n\n"

# Test 6: Stock out
echo "6. Removing stock..."
curl -X POST "$BASE_URL/inventory/items/1/stock-out" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "quantity": 3
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "=== All tests completed ==="
echo "Expected status codes: 201 for register/create, 200 for others"