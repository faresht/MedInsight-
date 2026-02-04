# MedInsight+ Deployment Guide

Complete guide for deploying the MedInsight+ E-Health System locally and in production.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Local Development Setup](#local-development-setup)
3. [Docker Compose Deployment](#docker-compose-deployment)
4. [Kubernetes Deployment](#kubernetes-deployment)
5. [Configuration](#configuration)
6. [Testing the Deployment](#testing-the-deployment)
7. [Troubleshooting](#troubleshooting)

---

## Prerequisites

### Required Software

- **Java**: JDK 21
- **Maven**: 3.9+
- **Docker**: 20.10+ with Docker Compose
- **Python**: 3.11+ (for AI service)
- **Node.js**: 18+ (for frontend)
- **kubectl**: Latest (for Kubernetes deployment)

### System Requirements

**Minimum**:
- CPU: 4 cores
- RAM: 8 GB
- Disk: 20 GB

**Recommended**:
- CPU: 8 cores
- RAM: 16 GB
- Disk: 50 GB

---

## Local Development Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd medinsight-backend
```

### 2. Build Commons Library

```bash
cd commons-library
mvn clean install
cd ..
```

### 3. Start Infrastructure Services

```bash
docker-compose up -d postgres mongodb redis kafka zookeeper keycloak elasticsearch
```

Wait for services to be healthy (~30 seconds):
```bash
docker-compose ps
```

### 4. Run Individual Services

**Backend Services** (in separate terminals):
```bash
# Gateway
cd gateway && mvn spring-boot:run

# Patient Service
cd patient-service && mvn spring-boot:run

# Doctor Service
cd doctor-service && mvn spring-boot:run

# Analytics Service
cd analytics-service && mvn spring-boot:run

# ... other services
```

**AI Service**:
```bash
cd ai-service
python -m venv venv
source venv/bin/activate  # Windows: venv\Scripts\activate
pip install -r requirements.txt
uvicorn app.main:app --reload --host 0.0.0.0 --port 8000
```

**Frontend**:
```bash
cd medinsight-frontend
npm install
npm start
```

---

## Docker Compose Deployment

### Quick Start

```bash
# Build all services
docker-compose build

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Check status
docker-compose ps
```

### Service Access Points

| Service | URL | Credentials |
|---------|-----|-------------|
| **API Gateway** | http://localhost:8080 | - |
| **Keycloak** | http://localhost:8180 | admin / admin |
| **Patient Service** | http://localhost:8081 | - |
| **Doctor Service** | http://localhost:8082 | - |
| **Analytics Service** | http://localhost:8084 | - |
| **AI Service** | http://localhost:8088 | - |
| **AI Service Docs** | http://localhost:8088/docs | - |
| **Prometheus** | http://localhost:9090 | - |
| **Grafana** | http://localhost:3000 | admin / admin |
| **Kibana** | http://localhost:5601 | - |
| **Zipkin** | http://localhost:9411 | - |

### Initial Setup

1. **Configure Keycloak**:
   - Access Keycloak at http://localhost:8180
   - Login with admin/admin
   - Create realm: `medinsight`
   - Create clients for each service
   - Create roles: MEDECIN, GESTIONNAIRE, RESPONSABLE_SECURITE, ADMIN
   - Create test users

2. **Verify AI Service**:
   ```bash
   curl http://localhost:8088/health
   ```

3. **Test Diagnosis Endpoint**:
   ```bash
   curl -X POST http://localhost:8088/api/v1/diagnose \
     -H "Content-Type: application/json" \
     -d '{
       "patient_id": "TEST001",
       "clinical_data": {
         "age": 55,
         "family_history": true,
         "bmi": 26.5,
         "menopause_status": "post"
       }
     }'
   ```

### Stopping Services

```bash
# Stop all services
docker-compose down

# Stop and remove volumes (WARNING: deletes data)
docker-compose down -v
```

---

## Kubernetes Deployment

### 1. Prepare Kubernetes Cluster

**Local (Minikube)**:
```bash
minikube start --memory=8192 --cpus=4
minikube addons enable ingress
```

**Cloud (GKE/EKS/AKS)**:
Follow cloud provider documentation to create cluster.

### 2. Create Namespace

```bash
kubectl create namespace medinsight
kubectl config set-context --current --namespace=medinsight
```

### 3. Deploy Infrastructure

```bash
# PostgreSQL
kubectl apply -f k8s/postgres/

# MongoDB
kubectl apply -f k8s/mongodb/

# Redis
kubectl apply -f k8s/redis/

# Kafka
kubectl apply -f k8s/kafka/

# Keycloak
kubectl apply -f k8s/keycloak/

# Elasticsearch
kubectl apply -f k8s/elasticsearch/

# Prometheus & Grafana
kubectl apply -f k8s/monitoring/
```

### 4. Deploy Microservices

```bash
# Gateway
kubectl apply -f k8s/gateway/

# Auth Service
kubectl apply -f k8s/auth/

# Patient Service
kubectl apply -f k8s/patient/

# Doctor Service
kubectl apply -f k8s/doctor/

# Analytics Service
kubectl apply -f k8s/analytics/

# AI Service
kubectl apply -f k8s/ai-service/

# Audit Service
kubectl apply -f k8s/audit/

# Notification Service
kubectl apply -f k8s/notification/

# Report Service
kubectl apply -f k8s/report/
```

### 5. Verify Deployment

```bash
# Check all pods
kubectl get pods

# Check services
kubectl get svc

# Check ingress
kubectl get ingress

# View logs
kubectl logs -f deployment/medinsight-ai-service
```

### 6. Access Services

**Port Forwarding** (for testing):
```bash
# AI Service
kubectl port-forward svc/ai-service 8088:8000

# Gateway
kubectl port-forward svc/gateway 8080:8080

# Grafana
kubectl port-forward svc/grafana 3000:3000
```

**Ingress** (production):
Services will be available at configured ingress hostnames.

---

## Configuration

### Environment Variables

**Backend Services**:
```properties
SPRING_PROFILES_ACTIVE=docker
DB_URL=jdbc:postgresql://postgres:5432/medinsight_db
KEYCLOAK_URL=http://keycloak:8080
MONGODB_URI=mongodb://admin:password@mongodb:27017/medinsight_analytics
```

**AI Service**:
```bash
MODEL_PATH=/app/data/models
LOG_LEVEL=INFO
```

### Keycloak Configuration

1. Create Realm: `medinsight`
2. Create Client: `medinsight-backend`
   - Client Protocol: openid-connect
   - Access Type: confidential
   - Valid Redirect URIs: `http://localhost:8080/*`
3. Create Roles:
   - MEDECIN
   - GESTIONNAIRE
   - RESPONSABLE_SECURITE
   - ADMIN
4. Create Users and assign roles

### Database Initialization

Databases are automatically initialized on first run with schema creation.

---

## Testing the Deployment

### 1. Health Checks

```bash
# Backend services
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
curl http://localhost:8084/actuator/health

# AI Service
curl http://localhost:8088/health
```

### 2. Authentication Test

```bash
# Get token from Keycloak
TOKEN=$(curl -X POST http://localhost:8180/realms/medinsight/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=medinsight-backend" \
  -d "client_secret=<your-secret>" \
  -d "username=doctor1" \
  -d "password=password" \
  -d "grant_type=password" | jq -r '.access_token')

# Use token
curl -H "Authorization: Bearer $TOKEN" http://localhost:8081/api/patients
```

### 3. AI Diagnosis Test

```bash
curl -X POST http://localhost:8088/api/v1/diagnose \
  -H "Content-Type: application/json" \
  -d @test-data/sample-diagnosis.json
```

### 4. End-to-End Test

1. Create a patient via Patient Service
2. Create a doctor via Doctor Service
3. Request breast cancer diagnosis via Analytics Service
4. Verify diagnosis result
5. Check audit logs in Kibana

---

## Troubleshooting

### Common Issues

**1. Services not starting**
```bash
# Check logs
docker-compose logs <service-name>

# Restart service
docker-compose restart <service-name>
```

**2. Database connection errors**
```bash
# Verify database is running
docker-compose ps postgres mongodb

# Check database logs
docker-compose logs postgres
```

**3. Keycloak connection issues**
```bash
# Verify Keycloak is healthy
curl http://localhost:8180/health

# Check Keycloak logs
docker-compose logs keycloak
```

**4. AI Service errors**
```bash
# Check AI service logs
docker-compose logs ai-service

# Verify Python dependencies
docker-compose exec ai-service pip list

# Test health endpoint
curl http://localhost:8088/health
```

**5. Out of memory**
```bash
# Increase Docker memory limit
# Docker Desktop -> Settings -> Resources -> Memory

# Or reduce services
docker-compose up -d postgres mongodb redis keycloak gateway patient-service ai-service
```

### Logs and Debugging

**View all logs**:
```bash
docker-compose logs -f
```

**View specific service logs**:
```bash
docker-compose logs -f ai-service
```

**Access container shell**:
```bash
docker-compose exec ai-service bash
```

**Check resource usage**:
```bash
docker stats
```

### Performance Optimization

1. **Increase JVM heap**:
   ```yaml
   environment:
     - JAVA_OPTS=-Xmx2g -Xms1g
   ```

2. **Enable caching**:
   - Redis for session storage
   - Application-level caching

3. **Database optimization**:
   - Add indexes
   - Connection pooling
   - Query optimization

---

## Production Deployment Checklist

- [ ] Change default passwords
- [ ] Configure TLS/HTTPS
- [ ] Set up backup strategy
- [ ] Configure monitoring alerts
- [ ] Enable MFA in Keycloak
- [ ] Set up log rotation
- [ ] Configure resource limits
- [ ] Set up auto-scaling (K8s HPA)
- [ ] Configure ingress with SSL
- [ ] Set up CI/CD pipeline
- [ ] Perform security audit
- [ ] Load testing
- [ ] Disaster recovery plan

---

## Support

For issues and questions:
- Check logs first
- Review this guide
- Consult technical report
- Contact development team

---

**Document Version**: 1.0
**Last Updated**: February 2026
