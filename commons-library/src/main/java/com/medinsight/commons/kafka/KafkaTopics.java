package com.medinsight.commons.kafka;

/**
 * Kafka topic name constants for MedInsight event-driven architecture
 */
public final class KafkaTopics {

    private KafkaTopics() {
        // Utility class
    }

    /**
     * Topic for audit events (user actions, security events, etc.)
     */
    public static final String AUDIT_EVENTS = "audit-events";

    /**
     * Topic for notification events (email, SMS, push notifications)
     */
    public static final String NOTIFICATION_EVENTS = "notification-events";

    /**
     * Dead Letter Queue for failed audit events
     */
    public static final String AUDIT_EVENTS_DLQ = "audit-events-dlq";

    /**
     * Dead Letter Queue for failed notification events
     */
    public static final String NOTIFICATION_EVENTS_DLQ = "notification-events-dlq";
}
