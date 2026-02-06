package com.medinsight.notification.repository;

import com.medinsight.notification.entity.Notification;
import com.medinsight.notification.enums.NotificationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByRecipientUserId(String recipientUserId);

    List<Notification> findByRecipientUserIdAndStatus(String recipientUserId, NotificationStatus status);

    List<Notification> findByStatus(NotificationStatus status);
}
