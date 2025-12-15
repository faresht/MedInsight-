package com.medinsight.notification.consumer;

import com.medinsight.commons.event.NotificationEvent;
import com.medinsight.commons.kafka.KafkaTopics;
import com.medinsight.notification.entity.Notification;
import com.medinsight.notification.enums.NotificationChannel;
import com.medinsight.notification.enums.NotificationStatus;
import com.medinsight.notification.enums.NotificationType;
import com.medinsight.notification.repository.NotificationRepository;
import com.medinsight.notification.service.EmailService;
import com.medinsight.notification.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Kafka consumer for notification events
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final SMSService smsService;

    @KafkaListener(topics = KafkaTopics.NOTIFICATION_EVENTS, groupId = "notification-service-group")
    public void consumeNotificationEvent(NotificationEvent event) {
        log.info("Consumed notification event: type={}, channel={}, recipientUserId={}",
                event.getType(), event.getChannel(), event.getRecipientUserId());

        try {
            // Convert NotificationEvent to Notification entity
            Notification notification = Notification.builder()
                    .recipientUserId(event.getRecipientUserId())
                    .type(NotificationType.valueOf(event.getType()))
                    .channel(NotificationChannel.valueOf(event.getChannel()))
                    .subject(event.getSubject())
                    .message(event.getMessage())
                    .status(NotificationStatus.PENDING)
                    .scheduledAt(event.getScheduledAt() != null ? event.getScheduledAt() : LocalDateTime.now())
                    .createdAt(LocalDateTime.now())
                    .build();

            // Save to PostgreSQL
            Notification savedNotification = notificationRepository.save(notification);
            log.info("Notification saved successfully: id={}, type={}",
                    savedNotification.getId(), savedNotification.getType());

            // Send notification based on channel
            sendNotification(savedNotification);

        } catch (Exception e) {
            log.error("Error processing notification event: {}", e.getMessage(), e);
            throw e; // Re-throw to trigger DLQ handling
        }
    }

    private void sendNotification(Notification notification) {
        try {
            switch (notification.getChannel()) {
                case EMAIL -> sendEmailNotification(notification);
                case SMS -> sendSMSNotification(notification);
                case PUSH, IN_APP -> log.info("PUSH/IN_APP notifications not yet implemented");
            }

            // Update status to SENT
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);

        } catch (Exception e) {
            // Update status to FAILED
            notification.setStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);
            log.error("Failed to send notification: {}", notification.getId(), e);
            throw e;
        }
    }

    private void sendEmailNotification(Notification notification) {
        // For welcome emails, use HTML template
        if ("PATIENT_WELCOME".equals(notification.getType().name())) {
            String htmlContent = emailService.createWelcomeEmailHtml("User", "");
            emailService.sendHtmlEmail("user@example.com", notification.getSubject(), htmlContent);
        } else {
            emailService.sendSimpleEmail("user@example.com", notification.getSubject(), notification.getMessage());
        }
    }

    private void sendSMSNotification(Notification notification) {
        smsService.sendSMS("+1234567890", notification.getMessage());
    }
}
