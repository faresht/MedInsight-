package com.medinsight.notification.service;

import com.medinsight.commons.exception.ResourceNotFoundException;
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

    public Notification createNotification(Notification notification) {
        notification.setStatus(NotificationStatus.PENDING);
        return notificationRepository.save(notification);
    }

    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
    }

    public List<Notification> getNotificationsByUserId(UUID userId) {
        return notificationRepository.findByRecipientUserId(userId);
    }

    public List<Notification> getPendingNotifications() {
        return notificationRepository.findByStatus(NotificationStatus.PENDING);
    }

    public Notification markAsSent(Long id) {
        Notification notification = getNotificationById(id);
        notification.setStatus(NotificationStatus.SENT);
        notification.setSentAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public Notification markAsDelivered(Long id) {
        Notification notification = getNotificationById(id);
        notification.setStatus(NotificationStatus.DELIVERED);
        return notificationRepository.save(notification);
    }

    public Notification markAsFailed(Long id) {
        Notification notification = getNotificationById(id);
        notification.setStatus(NotificationStatus.FAILED);
        return notificationRepository.save(notification);
    }
}
