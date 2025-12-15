package com.medinsight.commons.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * Common Kafka error handler that sends failed messages to Dead Letter Queue
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaErrorHandler implements CommonErrorHandler {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public boolean handleOne(Exception exception, ConsumerRecord<?, ?> record,
            Consumer<?, ?> consumer, MessageListenerContainer container) {
        log.error("Error processing Kafka message from topic: {}, partition: {}, offset: {}",
                record.topic(), record.partition(), record.offset(), exception);

        // Determine DLQ topic based on original topic
        String dlqTopic = getDlqTopic(record.topic());

        try {
            // Send to DLQ
            kafkaTemplate.send(dlqTopic, record.key(), record.value());
            log.info("Message sent to DLQ: {}", dlqTopic);
            return true; // Message handled, commit offset
        } catch (Exception e) {
            log.error("Failed to send message to DLQ: {}", dlqTopic, e);
            return false; // Don't commit offset, will retry
        }
    }

    private String getDlqTopic(String originalTopic) {
        if (KafkaTopics.AUDIT_EVENTS.equals(originalTopic)) {
            return KafkaTopics.AUDIT_EVENTS_DLQ;
        } else if (KafkaTopics.NOTIFICATION_EVENTS.equals(originalTopic)) {
            return KafkaTopics.NOTIFICATION_EVENTS_DLQ;
        }
        return originalTopic + "-dlq";
    }
}
