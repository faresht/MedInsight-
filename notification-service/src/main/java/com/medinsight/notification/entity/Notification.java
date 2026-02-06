package com.medinsight.notification.entity;

import com.medinsight.notification.enums.NotificationChannel;
import com.medinsight.notification.enums.NotificationStatus;
import com.medinsight.notification.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "notifications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    private String id;

    private String recipientUserId;

    private String recipientEmail;

    private NotificationType type;

    private NotificationChannel channel;

    private String subject;

    private String message;

    private NotificationStatus status;

    private LocalDateTime scheduledAt;

    private LocalDateTime sentAt;

    @CreatedDate
    private LocalDateTime createdAt;
}
