# Quick Docker Build & API Testing Guide

## 🚀 Build with Docker

### Step 1: Clean Start
```powershell
cd c:\Users\fares\.gemini\antigravity\scratch\medinsight-backend
docker-compose down
docker-compose up -d --build
```

This will:
- Build all services with Docker
- Start infrastructure (PostgreSQL, MongoDB, Redis, Keycloak, etc.)
- Start all 7 microservices

### Step 2: Wait & Verify
```powershell
# Wait 30 seconds for services to start

# Check running containers
docker ps

# Check logs
docker-compose logs -f patient-service
```

---

## 🧪 Test APIs

### Option 1: Using PowerShell (Invoke-RestMethod)

#### Test Patient Service

**Create a Patient:**
```powershell
$patient = @{
    userId = "123e4567-e89b-12d3-a456-426614174000"
    medicalRecordNumber = "MRN001"
    firstName = "John"
    lastName = "Doe"
    email = "john.doe@example.com"
    dateOfBirth = "1990-01-01"
    gender = "MALE"
    bloodType = "O+"
    portalActive = $true
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8081/api/patients" `
    -Method Post `
    -Body $patient `
    -ContentType "application/json"

$response
```

**Get All Patients:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/patients" -Method Get
```

**Get Patient by ID:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/patients/1" -Method Get
```

#### Test Doctor Service

**Create a Doctor:**
```powershell
$doctor = @{
    userId = "223e4567-e89b-12d3-a456-426614174000"
    firstName = "Jane"
    lastName = "Smith"
    email = "jane.smith@hospital.com"
    licenseNumber = "LIC12345"
    specialization = "Cardiology"
    experienceYears = 10
    active = $true
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8082/api/doctors" `
    -Method Post `
    -Body $doctor `
    -ContentType "application/json"
```

**Get All Doctors:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/doctors" -Method Get
```

**Get Doctors by Specialization:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8082/api/doctors/specialization/Cardiology" -Method Get
```

#### Test Auth Service

**Create a User:**
```powershell
$user = @{
    username = "patient1"
    email = "patient1@example.com"
    password = "SecurePass123"
    firstName = "Alice"
    lastName = "Johnson"
    userType = "PATIENT"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8083/api/users" `
    -Method Post `
    -Body $user `
    -ContentType "application/json"
```

---

### Option 2: Using curl (Git Bash or WSL)

#### Patient Service
```bash
# Create patient
curl -X POST http://localhost:8081/api/patients \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"123e4567-e89b-12d3-a456-426614174000",
    "medicalRecordNumber":"MRN001",
    "firstName":"John",
    "lastName":"Doe",
    "email":"john@example.com",
    "dateOfBirth":"1990-01-01",
    "gender":"MALE"
  }'

# Get all patients
curl http://localhost:8081/api/patients

# Get patient by ID
curl http://localhost:8081/api/patients/1
```

#### Doctor Service
```bash
# Create doctor
curl -X POST http://localhost:8082/api/doctors \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"223e4567-e89b-12d3-a456-426614174000",
    "firstName":"Jane",
    "lastName":"Smith",
    "email":"jane@hospital.com",
    "licenseNumber":"LIC123",
    "specialization":"Cardiology",
    "active":true
  }'

# Get all doctors
curl http://localhost:8082/api/doctors
```

---

### Option 3: Using Swagger UI (Easiest!)

Open in your browser:
```
http://localhost:8081/swagger-ui.html  (Patient Service)
http://localhost:8082/swagger-ui.html  (Doctor Service)
http://localhost:8083/swagger-ui.html  (Auth Service)
http://localhost:8084/swagger-ui.html  (Analytics Service)
```

**In Swagger UI:**
1. Click on any endpoint
2. Click "Try it out"
3. Fill in the request body
4. Click "Execute"
5. See the response

---

## 📊 Check Database

### PostgreSQL
```powershell
docker exec -it medinsight-postgres psql -U medinsight -d medinsight

# In psql:
SELECT * FROM patients;
SELECT * FROM doctors;
\q
```

### MongoDB
```powershell
docker exec -it medinsight-mongodb mongosh

# In mongosh:
use analytics
db.predictions.find()
exit
```

---

## 🔍 Troubleshooting

### Service won't start?
```powershell
# Check logs
docker-compose logs patient-service
docker-compose logs doctor-service

# Restart service
docker-compose restart patient-service
```

### Can't connect to API?
```powershell
# Check if service is running
docker ps | findstr patient

# Check service health
Invoke-RestMethod -Uri "http://localhost:8081/actuator/health"
```

### Clean restart
```powershell
docker-compose down -v
docker-compose up -d --build
```

---

## ✅ Quick Test Checklist

- [ ] Docker containers running (docker ps)
- [ ] Patient Service responds (http://localhost:8081/actuator/health)
- [ ] Can create a patient
- [ ] Can retrieve patients
- [ ] Doctor Service responds (http://localhost:8082/actuator/health)
- [ ] Can create a doctor
- [ ] Swagger UI accessible
- [ ] Data in PostgreSQL

---

## 🎯 Complete Workflow Test

```powershell
# 1. Create a user
$user = @{
    username = "doctor1"
    email = "doctor1@hospital.com"
    password = "Pass123"
    firstName = "Robert"
    lastName = "Brown"
    userType = "DOCTOR"
} | ConvertTo-Json

$newUser = Invoke-RestMethod -Uri "http://localhost:8083/api/users" `
    -Method Post -Body $user -ContentType "application/json"

# 2. Create a doctor with that user ID
$doctor = @{
    userId = $newUser.id
    firstName = "Robert"
    lastName = "Brown"
    email = "doctor1@hospital.com"
    licenseNumber = "LIC999"
    specialization = "General Medicine"
    active = $true
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8082/api/doctors" `
    -Method Post -Body $doctor -ContentType "application/json"

# 3. Create a patient
$patient = @{
    userId = "323e4567-e89b-12d3-a456-426614174000"
    medicalRecordNumber = "MRN002"
    firstName = "Alice"
    lastName = "Williams"
    email = "alice@example.com"
    dateOfBirth = "1985-05-15"
    gender = "FEMALE"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8081/api/patients" `
    -Method Post -Body $patient -ContentType "application/json"

# 4. Get all data
Write-Host "=== Patients ===" -ForegroundColor Green
Invoke-RestMethod -Uri "http://localhost:8081/api/patients" -Method Get

Write-Host "=== Doctors ===" -ForegroundColor Green
Invoke-RestMethod -Uri "http://localhost:8082/api/doctors" -Method Get
```

---

## 🎉 Success Criteria

If you can:
1. Run `docker ps` and see all containers
2. Access Swagger UI
3. Create and retrieve a patient
4. Create and retrieve a doctor
5. See data in PostgreSQL

**Your platform is working!** 🚀
