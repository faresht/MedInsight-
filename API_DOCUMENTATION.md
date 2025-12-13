# MedInsight+ E-Health Platform - API Documentation

## Overview
Complete REST API documentation for all 7 microservices with 60+ endpoints.

---

## Patient Service

### Base URL: `/api/patients`

#### Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/patients` | Create new patient |
| GET | `/api/patients/{id}` | Get patient by ID |
| GET | `/api/patients/user/{userId}` | Get patient by user ID |
| GET | `/api/patients` | Get all patients |
| PUT | `/api/patients/{id}` | Update patient |
| DELETE | `/api/patients/{id}` | Delete patient (soft delete) |
| GET | `/api/patients/exists/email/{email}` | Check if email exists |

### Medical Records: `/api/medical-records`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/medical-records` | Create medical record |
| GET | `/api/medical-records/patient/{patientId}` | Get patient's medical record |
| PUT | `/api/medical-records/{id}` | Update medical record |

---

## Doctor Service

### Doctors: `/api/doctors`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/doctors` | Create new doctor |
| GET | `/api/doctors/{id}` | Get doctor by ID |
| GET | `/api/doctors/user/{userId}` | Get doctor by user ID |
| GET | `/api/doctors` | Get all active doctors |
| GET | `/api/doctors/specialization/{specialization}` | Get doctors by specialization |
| PUT | `/api/doctors/{id}` | Update doctor |
| PUT | `/api/doctors/{id}/deactivate` | Deactivate doctor |

### Appointments: `/api/appointments`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/appointments` | Create appointment |
| GET | `/api/appointments/{id}` | Get appointment by ID |
| GET | `/api/appointments/patient/{patientId}` | Get patient's appointments |
| GET | `/api/appointments/doctor/{doctorId}` | Get doctor's appointments |
| GET | `/api/appointments/doctor/{doctorId}/range?start=&end=` | Get appointments in date range |
| PUT | `/api/appointments/{id}/status?status=` | Update appointment status |
| PUT | `/api/appointments/{id}/confirm` | Confirm appointment |
| PUT | `/api/appointments/{id}/cancel` | Cancel appointment |
| PUT | `/api/appointments/{id}/complete` | Complete appointment |

### Consultations: `/api/consultations`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/consultations` | Create consultation |
| GET | `/api/consultations/{id}` | Get consultation by ID |
| GET | `/api/consultations/appointment/{appointmentId}` | Get by appointment ID |
| GET | `/api/consultations/patient/{patientId}` | Get patient's consultations |
| GET | `/api/consultations/doctor/{doctorId}` | Get doctor's consultations |
| GET | `/api/consultations/patient/{patientId}/portal` | Get portal-visible consultations |
| PUT | `/api/consultations/{id}` | Update consultation |

---

## Auth Service

### Users: `/api/users`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create new user |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/username/{username}` | Get user by username |
| GET | `/api/users/email/{email}` | Get user by email |
| PUT | `/api/users/{id}` | Update user |
| PUT | `/api/users/{id}/disable` | Disable user account |
| PUT | `/api/users/{id}/last-login` | Update last login timestamp |

---

## Analytics Service

### AI Predictions: `/api/analytics`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/analytics/predict/relapse-risk` | Predict patient relapse risk |
| POST | `/api/analytics/predict/bed-occupancy` | Predict hospital bed occupancy |
| POST | `/api/analytics/detect/anomalies` | Detect security anomalies |

---

## Report Service

### Medical Reports: `/api/reports`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/reports` | Create medical report |
| GET | `/api/reports/{id}` | Get report by ID |
| GET | `/api/reports/patient/{patientId}` | Get patient's reports |
| GET | `/api/reports/doctor/{doctorId}` | Get doctor's reports |
| GET | `/api/reports/patient/{patientId}/type/{reportType}` | Get reports by patient and type |
| GET | `/api/reports/patient/{patientId}/portal` | Get portal-visible reports |
| PUT | `/api/reports/{id}` | Update report |
| DELETE | `/api/reports/{id}` | Delete report |

---

## Notification Service

### Notifications: `/api/notifications`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/notifications` | Create notification |
| GET | `/api/notifications/{id}` | Get notification by ID |
| GET | `/api/notifications/user/{userId}` | Get user's notifications |
| GET | `/api/notifications/pending` | Get all pending notifications |
| PUT | `/api/notifications/{id}/sent` | Mark notification as sent |
| PUT | `/api/notifications/{id}/delivered` | Mark notification as delivered |
| PUT | `/api/notifications/{id}/failed` | Mark notification as failed |

---

## Audit Service

### Audit Logs: `/api/audit`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/audit/user/{userId}` | Get audit logs for user |
| GET | `/api/audit/action/{action}` | Get logs by action type |
| GET | `/api/audit/entity/{entityType}` | Get logs by entity type |
| GET | `/api/audit/range?start=&end=` | Get logs in date range |

---

## Common Response Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 201 | Created - Resource created successfully |
| 204 | No Content - Successful deletion |
| 400 | Bad Request - Validation error |
| 404 | Not Found - Resource not found |
| 409 | Conflict - Duplicate resource |
| 500 | Internal Server Error |

---

## Error Response Format

```json
{
  "timestamp": "2025-12-13T11:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Patient not found with id: '123'",
  "path": "/api/patients/123"
}
```

## Validation Error Format

```json
{
  "timestamp": "2025-12-13T11:30:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "email": "Email should be valid",
    "firstName": "First name is required"
  },
  "path": "/api/patients"
}
```

---

## Swagger/OpenAPI

Access interactive API documentation at:
```
http://localhost:<port>/swagger-ui.html
```

OpenAPI specification:
```
http://localhost:<port>/v3/api-docs
```
