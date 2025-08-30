# API Testing Guide

## Issue Summary
You were experiencing two main problems:
1. **401 Unauthorized** - Missing JWT authentication token
2. **Validation Errors** - Address DTO validation constraints not properly configured

## What I Fixed

### 1. AddressDTO Validation
- Added proper validation annotations (`@NotBlank`, `@Size`)
- Added Lombok annotations for getters/setters
- Fixed validation messages to be consistent

### 2. Address Entity Validation
- Updated validation messages to be consistent with DTO
- Fixed minimum length requirements

### 3. Authentication Endpoint
- Fixed typo in login endpoint from `/signing` to `/signin`
- Added backward compatibility for the old endpoint

### 4. Address Service Logic
- Improved address creation logic to handle user-address relationships properly
- Added null checks and duplicate prevention

## How to Test

### Step 1: Get Authentication Token
First, you need to authenticate to get a JWT token:

```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "userName": "user1",
    "password": "password1"
  }'
```

**Response will include a JWT token in the Set-Cookie header.**

### Step 2: Create Address with Authentication
Use the JWT token from the cookie or extract it and use it in the Authorization header:

**Option A: Using Cookie (recommended)**
```bash
curl -X POST http://localhost:8080/api/address \
  -H "Content-Type: application/json" \
  -H "Cookie: jwt=<your-jwt-token>" \
  -d '{
    "street": "MG Road",
    "buildingName": "Sunrise Plaza",
    "city": "Bangalore",
    "state": "Karnataka",
    "country": "India",
    "pincode": "560001"
  }'
```

**Option B: Using Authorization Header**
```bash
curl -X POST http://localhost:8080/api/address \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "street": "MG Road",
    "buildingName": "Sunrise Plaza",
    "city": "Bangalore",
    "state": "Karnataka",
    "country": "India",
    "pincode": "560001"
  }'
```

## Test Users Available
The system automatically creates these test users:

- **Username:** `user1`, **Password:** `password1`
- **Username:** `seller1`, **Password:** `password2`  
- **Username:** `admin`, **Password:** `adminPass`

## Postman/Insomnia Setup

### For Authentication:
1. Create a POST request to `http://localhost:8080/api/auth/signin`
2. Set body to raw JSON:
```json
{
  "userName": "user1",
  "password": "password1"
}
```

### For Address Creation:
1. Create a POST request to `http://localhost:8080/api/address`
2. Add header: `Authorization: Bearer <your-jwt-token>`
3. Set body to raw JSON with your address data

## Common Issues & Solutions

### 1. Still getting 401?
- Make sure you're using the correct JWT token
- Check that the token hasn't expired
- Verify the Authorization header format: `Bearer <token>`

### 2. Getting validation errors?
- Ensure all required fields are provided
- Check field lengths meet minimum requirements:
  - Street: minimum 5 characters
  - Building Name: minimum 4 characters
  - City: minimum 4 characters
  - State: minimum 4 characters
  - Country: minimum 4 characters
  - Pincode: minimum 4 characters

### 3. JWT token expired?
- Re-authenticate to get a new token
- Check your application.properties for JWT expiration settings

## Security Notes
- The `/api/address` endpoint requires authentication
- JWT tokens are stateless and secure
- Always use HTTPS in production
- Tokens have expiration times for security
