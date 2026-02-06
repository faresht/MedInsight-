package com.medinsight.notification.service;

import com.medinsight.notification.exception.ResourceNotFoundException;
import com.medinsight.notification.entity.Notification;
import com.medinsight.notification.enums.NotificationStatus;
import com.medinsight.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public Notification createNotification(Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);
        try {
            // Send email immediately (for simplicity in this phase)
            // In production, this might be async via Kafka consumer
            emailService.sendEmail(
                    notification.getRecipientEmail(),
                    notification.getSubject(),
                    notification.getMessage());
            savedNotification.setStatus(NotificationStatus.SENT);
            savedNotification.setSentAt(LocalDateTime.now());
        } catch (Exception e) {
            savedNotification.setStatus(NotificationStatus.FAILED);
        }
        return notificationRepository.save(savedNotification);
    }

    public Notification getNotificationById(String id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
    }

    public List<Notification> getNotificationsByUserId(String userId) {
        return notificationRepository.findByRecipientUserId(userId);
    }

    public List<Notification> getPendingNotifications() {
        return notificationRepository.findByStatus(NotificationStatus.PENDING);
    }

    public Notification markAsSent(String id) {
        Notification notification = getNotificationById(id);
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public Notification markAsDelivered(String id) {
        Notification notification = getNotificationById(id);
        notification.setStatus(NotificationStatus.DELIVERED);
        return notificationRepository.save(notification);
    }

    public Notification markAsFailed(String id) {
        Notification notification = getNotificationById(id);
        notification.setStatus(NotificationStatus.FAILED);
        return notificationRepository.save(notification);
    }
}
