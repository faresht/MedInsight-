package com.medinsight.audit.document;

import com.medinsight.audit.enums.AuditAction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Document(collection = "audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    private String id;

    private UUID userId;

    private String username;

    private AuditAction action;

    private String entityType;

    private String entityId;

    private String ipAddress;

    private String userAgent;

    private Map<String, Object> changes;

    private LocalDateTime timestamp;
}
