# Hospital Management System

A comprehensive Hospital Management System built with Java and Spring Boot for managing hospital operations, patient records, appointments, and medical staff.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Authentication](#authentication)
- [Error Handling](#error-handling)
- [Development](#development)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

## Overview

Hospital Management System is a robust, scalable application designed to streamline hospital operations. It handles patient management, doctor scheduling, appointment booking, medical records, billing, and inventory management. The system provides secure access through role-based authentication and maintains comprehensive audit logs for compliance.

## Features

- **Patient Management**: Register, update, and manage patient information
- **Doctor Management**: Manage doctor profiles and specializations
- **Appointment Scheduling**: Book and manage medical appointments
- **Medical Records**: Maintain comprehensive patient medical history
- **Prescription Management**: Generate and manage prescriptions
- **Billing System**: Invoice generation and payment tracking
- **Inventory Management**: Track medical supplies and equipment
- **Department Management**: Organize and manage hospital departments
- **Staff Management**: Manage hospital staff and roles
- **Reports**: Generate various medical and administrative reports
- **Security**: JWT authentication and role-based access control
- **Audit Logging**: Complete audit trail for all operations

## Technology Stack

- **Java**: 11+ (92.7% of codebase)
- **Framework**: Spring Boot 2.7+
- **Build Tool**: Maven
- **Database**: MySQL/PostgreSQL
- **Security**: Spring Security with JWT
- **API Documentation**: Swagger/OpenAPI
- **ORM**: Spring Data JPA / Hibernate
- **Logging**: SLF4J with Logback
- **Frontend**: HTML5 (7.3% of codebase)
- **PDF Generation**: iText or Apache PDFBox

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven 3.6 or higher
- MySQL 5.7+ or PostgreSQL 10+
- Git

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/MAROOTS/hospital.git
cd hospital
```

### 2. Configure Database

Create a MySQL database:

```sql
CREATE DATABASE hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. Update Application Configuration

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hospital_db
    username: root
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
  application:
    name: hospital-management
```

### 4. Build the Application

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## Configuration

### Application Properties

Key configuration properties in `application.yml`:

```yaml
server:
  port: 8080
  servlet:
    context-path: /api/v1

logging:
  level:
    root: INFO
    com.hospital: DEBUG

jwt:
  secret: your-secret-key-here
  expiration: 86400000  # 24 hours in milliseconds

hospital:
  appointment:
    duration-minutes: 30
    max-per-day: 50
  billing:
    tax-rate: 0.05
    payment-terms: 30
  inventory:
    low-stock-threshold: 10
    reorder-quantity: 100
```

### Security Configuration

The application uses JWT tokens for authentication. Include the token in the Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## API Endpoints

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/login` | User login |
| POST | `/api/v1/auth/register` | Register new user |
| POST | `/api/v1/auth/logout` | User logout |
| POST | `/api/v1/auth/refresh` | Refresh JWT token |

### Patient Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/patients` | List all patients |
| GET | `/api/v1/patients/{patientId}` | Get patient details |
| POST | `/api/v1/patients` | Register new patient |
| PUT | `/api/v1/patients/{patientId}` | Update patient information |
| DELETE | `/api/v1/patients/{patientId}` | Delete patient record |
| GET | `/api/v1/patients/{patientId}/medical-history` | Get patient medical history |

### Doctor Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/doctors` | List all doctors |
| GET | `/api/v1/doctors/{doctorId}` | Get doctor details |
| POST | `/api/v1/doctors` | Add new doctor |
| PUT | `/api/v1/doctors/{doctorId}` | Update doctor information |
| DELETE | `/api/v1/doctors/{doctorId}` | Remove doctor |
| GET | `/api/v1/doctors/{doctorId}/availability` | Get doctor's availability |

### Appointments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/appointments` | List all appointments |
| GET | `/api/v1/appointments/{appointmentId}` | Get appointment details |
| POST | `/api/v1/appointments` | Book new appointment |
| PUT | `/api/v1/appointments/{appointmentId}` | Update appointment |
| DELETE | `/api/v1/appointments/{appointmentId}` | Cancel appointment |
| GET | `/api/v1/appointments/doctor/{doctorId}` | Get doctor's appointments |
| GET | `/api/v1/appointments/patient/{patientId}` | Get patient's appointments |

### Medical Records

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/medical-records` | List all records |
| GET | `/api/v1/medical-records/{recordId}` | Get record details |
| POST | `/api/v1/medical-records` | Create new medical record |
| PUT | `/api/v1/medical-records/{recordId}` | Update medical record |
| DELETE | `/api/v1/medical-records/{recordId}` | Delete medical record |

### Prescriptions

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/prescriptions` | List all prescriptions |
| GET | `/api/v1/prescriptions/{prescriptionId}` | Get prescription details |
| POST | `/api/v1/prescriptions` | Issue new prescription |
| PUT | `/api/v1/prescriptions/{prescriptionId}` | Update prescription |
| DELETE | `/api/v1/prescriptions/{prescriptionId}` | Cancel prescription |

### Billing

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/bills` | List all bills |
| GET | `/api/v1/bills/{billId}` | Get bill details |
| POST | `/api/v1/bills` | Generate new bill |
| PUT | `/api/v1/bills/{billId}` | Update bill |
| POST | `/api/v1/bills/{billId}/payment` | Record payment |
| GET | `/api/v1/bills/patient/{patientId}` | Get patient's bills |

### Departments

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/departments` | List all departments |
| GET | `/api/v1/departments/{departmentId}` | Get department details |
| POST | `/api/v1/departments` | Create new department |
| PUT | `/api/v1/departments/{departmentId}` | Update department |
| DELETE | `/api/v1/departments/{departmentId}` | Delete department |

### Inventory

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/inventory` | List all inventory items |
| GET | `/api/v1/inventory/{itemId}` | Get item details |
| POST | `/api/v1/inventory` | Add new inventory item |
| PUT | `/api/v1/inventory/{itemId}` | Update inventory item |
| DELETE | `/api/v1/inventory/{itemId}` | Remove inventory item |
| POST | `/api/v1/inventory/{itemId}/reorder` | Reorder item |

### Reports

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/reports/patient-summary` | Patient summary report |
| GET | `/api/v1/reports/revenue` | Revenue report |
| GET | `/api/v1/reports/appointments` | Appointment statistics |
| GET | `/api/v1/reports/doctor-performance` | Doctor performance report |
| GET | `/api/v1/reports/inventory-status` | Inventory status report |

## Database Schema

### Core Tables

```
Patients
├── patientId (PK)
├── firstName
├── lastName
├── email
├── phone
├── dateOfBirth
├── gender
├── address
└── registrationDate

Doctors
├── doctorId (PK)
├── firstName
├── lastName
├── specialization
├── departmentId (FK)
├── email
├── phone
└── licenseNumber

Appointments
├── appointmentId (PK)
├── patientId (FK)
├── doctorId (FK)
├── appointmentDate
├── appointmentTime
├── status
└── notes

MedicalRecords
├── recordId (PK)
├── patientId (FK)
├── doctorId (FK)
├── appointmentId (FK)
├── diagnosis
├── treatment
├── notes
└── recordDate

Departments
├── departmentId (PK)
├── departmentName
├── description
└── headDoctorId (FK)

Prescriptions
├── prescriptionId (PK)
├── patientId (FK)
├── doctorId (FK)
├── medicationName
├── dosage
├── frequency
├── duration
└── prescriptionDate
```

## Authentication

### Login Request

```json
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "doctor@hospital.com",
  "password": "password123"
}
```

### Login Response

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400000,
  "user": {
    "userId": 1,
    "username": "doctor@hospital.com",
    "role": "DOCTOR"
  },
  "message": "Login successful"
}
```

## Error Handling

The application uses standard HTTP status codes and returns errors in the following format:

```json
{
  "timestamp": "2024-06-22T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Patient with ID 123 not found",
  "path": "/api/v1/patients/123"
}
```

### Common Error Codes

| Status | Error | Description |
|--------|-------|-------------|
| 400 | Bad Request | Invalid request parameters |
| 401 | Unauthorized | Missing or invalid authentication |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource already exists |
| 422 | Unprocessable Entity | Invalid data |
| 500 | Internal Server Error | Server error |

## Development

### Project Structure

```
hospital/
├── src/
│   ├── main/
│   │   ├── java/com/hospital/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── entity/
│   │   │   ├── dto/
│   │   │   ├── security/
│   │   │   ├── util/
│   │   │   └── config/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── db/migration/
│   └── test/
├── pom.xml
└── README.md
```

### Running Tests

```bash
mvn test
```

### Building for Production

```bash
mvn clean package -DskipTests
java -jar target/hospital-management-1.0.0.jar
```

## Troubleshooting

### Database Connection Error
- Verify MySQL/PostgreSQL is running
- Check database credentials in `application.yml`
- Ensure database exists and migrations have run

### Port 8080 Already in Use
```bash
# Change port in application.yml or use
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Authentication Failures
- Verify JWT secret key is configured
- Check token expiration
- Ensure Authorization header format is correct
- Verify user role and permissions

### Appointment Booking Issues
- Verify doctor availability
- Check appointment slot availability
- Ensure patient and doctor records exist
- Verify no scheduling conflicts

### Billing Issues
- Verify patient has active account
- Check billing amounts and calculations
- Ensure payment gateway integration (if applicable)

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style Guidelines

- Follow Java naming conventions
- Use meaningful variable names
- Write Javadoc for public methods
- Keep methods focused and concise
- Add unit tests for new features
- Follow HIPAA compliance guidelines for patient data

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or suggestions, please open an issue on the GitHub repository or contact the development team.

---

**Last Updated**: June 2026
**Version**: 1.0.0
