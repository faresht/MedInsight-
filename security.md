# MedInsight+ Security Architecture

## Principles
- **Zero Trust**: All internal traffic is authenticated (future: mTLS).
- **Defense in Depth**: Gateway + Service Level Security.
- **Traceability**: All actions logged to Audit Service.

## Implementation

### Authentication
- **Protocol**: OAuth2 / OpenID Connect.
- **Provider**: Keycloak.
- **Flow**: Authorization Code Flow with PKCE for frontend; Client Credentials for service-to-service.
- **Token**: JWT (Stateless).

### Authorization
- **RBAC**: Roles defined in Keycloak (doctor, manager, sec-officer).
- **Enforcement**: `@PreAuthorize` annotations in Spring Boot controllers.

### Data Security
- **At Rest**: Database encryption (TBD), Encrypted volumes in K8s.
- **In Transit**: TLS 1.3 for external; HTTP for internal (mTLS recommended for prod).

### DevSecOps
- **SAST**: SonarQube in CI pipeline.
- **SCA**: Dependency Check / Trivy for containers.
- **Audit**: Centralized logging via ELK.
