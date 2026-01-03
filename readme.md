# Spring Security Authentication Template

A production-ready Spring Boot authentication and authorization template with email verification, password reset, and JWT-based security.

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Testing](#testing)
- [Contributing](#contributing)

## Overview

This project provides a complete authentication solution that can be integrated into any Spring Boot application. It includes user registration, login, email verification, password management, and role-based access control.

## Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security 6.x**
- **PostgreSQL 14+**
- **Maven 3.8+**
- **Swagger/OpenAPI 3.0**
- **JWT (JSON Web Tokens)**
- **JUnit 5**
- **Mockito**
- **JavaMail**

## Features

- ✅ User Registration with Email Verification
- ✅ User Login with JWT Authentication
- ✅ Password Reset Flow
- ✅ Role-Based Access Control (RBAC)
- ✅ Refresh Token Mechanism
- ✅ User Profile Management
- ✅ Account Activation/Deactivation
- ✅ Comprehensive API Documentation
- ✅ Full Test Coverage

## Getting Started

### Prerequisites

```bash
- JDK 17 or higher
- Maven 3.8+
- PostgreSQL 14+
- SMTP Server (for email functionality)
```

### Installation

1. **Clone the repository**

```bash
git clone https://github.com/Adwoa-p/spring-security-template.git
cd spring-security-template
```

2. **Configure Database**

Create a PostgreSQL database:

```sql
CREATE DATABASE auth_template;
```

3. **Update Application Properties**

Edit `src/main/resources/application.properties`:

```properties
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/auth_template
    username: your_username
    password: your_password
  
  mail:
    host: smtp.gmail.com
    port: 587
    username: your_email@gmail.com
    password: your_app_password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

jwt:
  secret: 256-bit-secret-key
  expiration: 3600000

app:
  base-url: http://localhost:8080
```

4. **Build and Run**

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### API Documentation

Once running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

## API Documentation

### Base URL

```
http://localhost:8080/api/v1
```

### Authentication Endpoints

All authentication endpoints are **unprotected** (no JWT required).

#### 1. Register User

**Endpoint:** `POST /api/v1/auth/signup`

**Description:** Register a new user account. Sends verification email.

**Request Body:**

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123!"
}
```

**Response:** `201 Created`

```json
{
  "status": "success",
  "message": "User registered successfully. Please check your email to verify your account."
}
```

**Error Responses:**

- `400 Bad Request` - Invalid input or email already exists
- `500 Internal Server Error` - Server error

---

#### 2. Verify Email

**Endpoint:** `GET /api/v1/auth/verify-email`

**Description:** Verify user email address using token sent via email.

**Query Parameters:**

- `token` (required) - Email verification token

**Example:**
```
GET /api/v1/auth/verify-email?token=abc123xyz789
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Email verified successfully. You can now login.",
  "data": {
    "email": "john.doe@example.com",
    "emailVerified": true,
    "verifiedAt": "2026-01-02T10:35:00Z"
  }
}
```

**Error Responses:**

- `400 Bad Request` - Invalid or expired token
- `404 Not Found` - Token not found

---

#### 3. Resend Verification Email

**Endpoint:** `POST /api/v1/auth/resend-verification`

**Description:** Resend email verification link.

**Request Body:**

```json
{
  "email": "john.doe@example.com"
}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Verification email sent successfully.",
  "data": {
    "email": "john.doe@example.com",
    "sentAt": "2026-01-02T10:40:00Z"
  }
}
```

---

#### 4. Login

**Endpoint:** `POST /api/v1/auth/login`

**Description:** Authenticate user and receive JWT tokens.

**Request Body:**

```json
{
  "email": "john.doe@example.com",
  "password": "SecurePassword123!"
}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Login successful",
  "token": "bfrjqeyr31837eudhsabmn8"
}
```

**Error Responses:**

- `401 Unauthorized` - Invalid credentials
- `403 Forbidden` - Email not verified or account disabled

---

#### 5. Refresh Token

**Endpoint:** `POST /api/v1/auth/refresh-token`

**Description:** Get new access token using refresh token.

**Request Body:**

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400
  }
}
```

---

#### 6. Forgot Password

**Endpoint:** `POST /api/v1/auth/forgot-password`

**Description:** Request password reset email.

**Request Body:**

```json
{
  "email": "john.doe@example.com"
}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Password reset link sent to your email.",
  "data": {
    "email": "john.doe@example.com",
    "sentAt": "2026-01-02T11:00:00Z"
  }
}
```

---

#### 7. Reset Password

**Endpoint:** `POST /api/v1/auth/reset-password`

**Description:** Reset password using token from email.

**Request Body:**

```json
{
  "token": "abc123xyz789",
  "newPassword": "NewSecurePassword123!",
  "confirmPassword": "NewSecurePassword123!"
}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Password reset successfully. You can now login with your new password.",
  "data": {
    "email": "john.doe@example.com",
    "resetAt": "2026-01-02T11:05:00Z"
  }
}
```

---

#### 8. Logout

**Endpoint:** `POST /api/v1/auth/logout`

**Description:** Logout user and invalidate tokens.

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Logged out successfully",
  "data": null
}
```

---

### User Management Endpoints

All user endpoints are **protected** and require JWT authentication.

#### 9. Get Current User Profile

**Endpoint:** `GET /api/v1/users/me`

**Description:** Get authenticated user's profile.

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "User profile retrieved successfully",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "emailVerified": true,
    "roles": ["ROLE_USER"],
    "enabled": true,
    "createdAt": "2026-01-02T10:30:00Z",
    "updatedAt": "2026-01-02T10:30:00Z"
  }
}
```

---

#### 10. Update User Profile

**Endpoint:** `PUT /api/v1/users/me`

**Description:** Update authenticated user's profile information.

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**

```json
{
  "firstName": "John",
  "lastName": "Smith",
  "phoneNumber": "+1234567890"
}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Profile updated successfully",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Smith",
    "phoneNumber": "+1234567890",
    "updatedAt": "2026-01-02T11:30:00Z"
  }
}
```

---

#### 11. Change Password

**Endpoint:** `PUT /api/v1/users/me/change-password`

**Description:** Change user password (requires current password).

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**

```json
{
  "currentPassword": "SecurePassword123!",
  "newPassword": "NewSecurePassword456!",
  "confirmPassword": "NewSecurePassword456!"
}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Password changed successfully",
  "data": {
    "changedAt": "2026-01-02T11:45:00Z"
  }
}
```

---

#### 12. Delete Account

**Endpoint:** `DELETE /api/v1/users/me`

**Description:** Soft delete user account.

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**

```json
{
  "password": "SecurePassword123!",
  "confirmation": "DELETE"
}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Account deleted successfully",
  "data": {
    "deletedAt": "2026-01-02T12:00:00Z"
  }
}
```

---

### Admin Endpoints

Admin endpoints require `ROLE_ADMIN` authority.

#### 13. Get All Users

**Endpoint:** `GET /api/v1/admin/users`

**Description:** Get paginated list of all users.

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Query Parameters:**

- `page` (optional, default: 0) - Page number
- `size` (optional, default: 20) - Page size
- `sort` (optional, default: createdAt,desc) - Sort criteria

**Example:**
```
GET /api/v1/admin/users?page=0&size=10&sort=email,asc
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "Users retrieved successfully",
  "data": {
    "content": [
      {
        "userId": "550e8400-e29b-41d4-a716-446655440000",
        "email": "john.doe@example.com",
        "firstName": "John",
        "lastName": "Doe",
        "roles": ["ROLE_USER"],
        "enabled": true,
        "emailVerified": true,
        "createdAt": "2026-01-02T10:30:00Z"
      }
    ],
    "pageable": {
      "pageNumber": 0,
      "pageSize": 10,
      "sort": {
        "sorted": true,
        "unsorted": false
      }
    },
    "totalPages": 1,
    "totalElements": 5,
    "last": true,
    "first": true
  }
}
```

---

#### 14. Get User By ID

**Endpoint:** `GET /api/v1/admin/users/{userId}`

**Description:** Get specific user details by ID.

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "User retrieved successfully",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "email": "john.doe@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "roles": ["ROLE_USER"],
    "enabled": true,
    "emailVerified": true,
    "createdAt": "2026-01-02T10:30:00Z",
    "updatedAt": "2026-01-02T11:30:00Z"
  }
}
```

---

#### 15. Update User Roles

**Endpoint:** `PUT /api/v1/admin/users/{userId}/roles`

**Description:** Update user roles.

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**

```json
{
  "roles": ["ROLE_USER", "ROLE_ADMIN"]
}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "User roles updated successfully",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "roles": ["ROLE_USER", "ROLE_ADMIN"],
    "updatedAt": "2026-01-02T12:30:00Z"
  }
}
```

---

#### 16. Enable/Disable User Account

**Endpoint:** `PATCH /api/v1/admin/users/{userId}/status`

**Description:** Enable or disable user account.

**Headers:**
```
Authorization: Bearer {accessToken}
```

**Request Body:**

```json
{
  "enabled": false
}
```

**Response:** `200 OK`

```json
{
  "status": "success",
  "message": "User account status updated successfully",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000",
    "enabled": false,
    "updatedAt": "2026-01-02T12:45:00Z"
  }
}
```

---

## Security

### Authentication Flow

1. **Registration**: User registers → Email verification sent
2. **Verification**: User clicks link → Account activated
3. **Login**: User logs in → Receives JWT access & refresh tokens
4. **Access**: User includes JWT in Authorization header for protected endpoints
5. **Refresh**: When access token expires, use refresh token to get new access token

### Authorization

The application implements role-based access control (RBAC) with the following roles:

- `ROLE_USER` - Standard user access
- `ROLE_ADMIN` - Administrative access

### Endpoint Security Summary

#### Unprotected Endpoints (No Authentication Required)

- `POST /api/v1/auth/register`
- `GET /api/v1/auth/verify-email`
- `POST /api/v1/auth/resend-verification`
- `POST /api/v1/auth/login`
- `POST /api/v1/auth/refresh-token`
- `POST /api/v1/auth/forgot-password`
- `POST /api/v1/auth/reset-password`

#### Protected Endpoints (Authentication Required)

**User Endpoints** - Requires `ROLE_USER`:
- `GET /api/v1/users/me`
- `PUT /api/v1/users/me`
- `PUT /api/v1/users/me/change-password`
- `DELETE /api/v1/users/me`
- `POST /api/v1/auth/logout`

**Admin Endpoints** - Requires `ROLE_ADMIN`:
- `GET /api/v1/admin/users`
- `GET /api/v1/admin/users/{userId}`
- `PUT /api/v1/admin/users/{userId}/roles`
- `PATCH /api/v1/admin/users/{userId}/status`

### JWT Token Format

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## Testing

### Running Tests

```bash
# Run all tests
mvn test
```

### Test Coverage

The project includes comprehensive tests for:

- **Controller Layer**: API endpoint testing
- **Service Layer**: Business logic testing
- **Repository Layer**: Database operations
- **Security**: Authentication and authorization
- **Unit Tests**: End-to-end scenarios

### Test Structure

```
src/test/java/
├── controller/
│   ├── AuthControllerTest.java
│   ├── UserControllerTest.java
│   └── AdminControllerTest.java
├── service/
│   ├── AuthServiceTest.java
│   ├── UserServiceTest.java
│   └── EmailServiceTest.java
├── security/
├── ├── JwtTokenProviderTest.java

```

## Common Error Responses

All endpoints follow a consistent error response format:

```json
{
  "status": "error",
  "message": "Error description",
  "errors": [
    {
      "field": "email",
      "message": "Email is already registered"
    }
  ],
  "timestamp": "2026-01-02T12:00:00Z",
  "path": "/api/v1/auth/register"
}
```

### HTTP Status Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request data
- `401 Unauthorized` - Authentication required or failed
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Resource conflict (e.g., duplicate email)
- `500 Internal Server Error` - Server error

## Environment Variables

```properties
# Database
DB_URL=jdbc:postgresql://localhost:5432/db-name
DB_USER=postgres
DB_PASSWORD=password

# JWT
JWT_SECRET=secret-key

# Email
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=email@gmail.com
MAIL_PASSWORD=ypassword

```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request


## Support

For issues, questions, or contributions, please open an issue on GitHub.

---

**Note**: This is a template project. Remember to update security configurations, secrets, and database credentials before deploying to production.