# MedInsight+ Architecture

## Overview
MedInsight+ is a secure, microservices-based E-Health platform designed for predictive analytics, patient management, and medical reporting.

## Components

### Microservices
| Service | Function | Tech Stack |
|---------|----------|------------|
| **Gateway** | Entry point, Routing, Rate Limiting, Token Relay | Spring Cloud Gateway, Redis |
| **Auth Service** | Identity Wrapper | Keycloak, Spring Boot |
| **Patient Service** | Patient CRUD, Auditing | PostgreSQL, Spring Data JPA |
| **Doctor Service** | Doctor Profiles, Schedules | PostgreSQL |
| **Report Service** | PDF Generation, Summaries | PostgreSQL |
| **Analytics Service** | AI Predictions, RAG | MongoDB, Spring AI, Ollama (Gwen 2.5) |
| **Audit Service** | Security Logs, Traceability | MongoDB, ELK |
| **Notification Service** | Alerts (Email/Webhook) | Kafka |

### Infrastructure
- **Service Discovery**: Kubernetes / Docker DNS
- **Config Management**: K8s ConfigMaps / Secrets
- **Identity**: Keycloak (OIDC/OAuth2)
- **AI**: Ollama running locally or in cluster
- **Observability**: ELK Stack, Prometheus, Grafana

## Diagram
```mermaid
graph TD
    User[User (Web/Mobile)] -->|HTTPS| Gateway
    Gateway -->|Auth| Keycloak
    Gateway -->|/api/patients| PatientService
    Gateway -->|/api/doctors| DoctorService
    Gateway -->|/api/analytics| AnalyticsService
    
    AnalyticsService -->|Prompt| Ollama[Ollama (Gwen 2.5)]
    AnalyticsService -->|Read/Write| MongoDB
    
    PatientService -->|Read/Write| Postgres
    DoctorService -->|Read/Write| Postgres
    
    AuditService -->|Logs| Elasticsearch
    AllServices -->|Events| Kafka
    AllServices -->|Logs| AuditService
```
