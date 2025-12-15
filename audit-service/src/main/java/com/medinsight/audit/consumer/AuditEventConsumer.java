package com.medinsight.audit.consumer;

import com.medinsight.audit.document.AuditLog;
import com.medinsight.audit.enums.AuditAction;
import com.medinsight.audit.repository.AuditLogRepository;
import com.medinsight.commons.event.AuditEvent;
import com.medinsight.commons.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer for audit events
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventConsumer {

    private final AuditLogRepository auditLogRepository;

    @KafkaListener(topics = KafkaTopics.AUDIT_EVENTS, groupId = "audit-service-group")
    public void consumeAuditEvent(AuditEvent event) {
        log.info("Consumed audit event: action={}, entityType={}, userId={}",
                event.getAction(), event.getEntityType(), event.getUserId());

        try {
            // Convert AuditEvent to AuditLog document
            AuditLog auditLog = AuditLog.builder()
                    .userId(event.getUserId())
                    .username(event.getUsername())
                    .action(AuditAction.valueOf(event.getAction()))
                    .entityType(event.getEntityType())
                    .entityId(event.getEntityId())
                    .ipAddress(event.getIpAddress())
                    .userAgent(event.getUserAgent())
                    .changes(event.getChanges())
                    .timestamp(event.getTimestamp())
                    .build();

            // Save to MongoDB
            auditLogRepository.save(auditLog);
            log.info("Audit log saved successfully: id={}", auditLog.getId());

        } catch (Exception e) {
            log.error("Error processing audit event: {}", e.getMessage(), e);
            // In production, you might want to send to a dead letter queue
        }
    }
}
