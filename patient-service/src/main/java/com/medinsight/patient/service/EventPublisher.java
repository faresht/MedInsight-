package com.medinsight.patient.service;

import com.medinsight.commons.event.AuditEvent;
import com.medinsight.commons.event.NotificationEvent;
import com.medinsight.commons.kafka.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Service to publish events to Kafka
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Publish audit event to Kafka
     */
    public void publishAuditEvent(UUID userId, String username, String action,
            String entityType, String entityId,
            String ipAddress, String userAgent,
            Map<String, Object> changes) {
        AuditEvent event = AuditEvent.builder()
                .userId(userId)
                .username(username)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .changes(changes)
                .timestamp(LocalDateTime.now())
                .build();

        kafkaTemplate.send(KafkaTopics.AUDIT_EVENTS, event);
        log.info("Published audit event: action={}, entityType={}, entityId={}",
                action, entityType, entityId);
    }

    /**
     * Publish notification event to Kafka
     */
    public void publishNotificationEvent(UUID recipientUserId, String type,
            String channel, String subject,
            String message) {
        NotificationEvent event = NotificationEvent.builder()
                .recipientUserId(recipientUserId)
                .type(type)
                .channel(channel)
                .subject(subject)
                .message(message)
                .scheduledAt(LocalDateTime.now())
                .build();

        kafkaTemplate.send(KafkaTopics.NOTIFICATION_EVENTS, event);
        log.info("Published notification event: type={}, channel={}, recipientUserId={}",
                type, channel, recipientUserId);
    }
}
