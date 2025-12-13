package com.medinsight.notification.repository;

import com.medinsight.notification.entity.Notification;
import com.medinsight.notification.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientUserId(UUID recipientUserId);

    List<Notification> findByRecipientUserIdAndStatus(UUID recipientUserId, NotificationStatus status);

    List<Notification> findByStatus(NotificationStatus status);
}
