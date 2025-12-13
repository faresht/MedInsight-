package com.medinsight.audit.document;

import com.medinsight.audit.enums.AlertSeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "security_alerts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityAlert {

    @Id
    private String id;

    private AlertSeverity severity;

    private String alertType;

    private String description;

    private String sourceIp;

    private UUID userId;

    private boolean resolved;

    private LocalDateTime detectedAt;

    private LocalDateTime resolvedAt;
}
