package com.medinsight.notification.controller;

import com.medinsight.notification.entity.Notification;
import com.medinsight.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification created = notificationService.createNotification(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        Notification notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable UUID userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Notification>> getPendingNotifications() {
        List<Notification> notifications = notificationService.getPendingNotifications();
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/sent")
    public ResponseEntity<Notification> markAsSent(@PathVariable Long id) {
        Notification notification = notificationService.markAsSent(id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/{id}/delivered")
    public ResponseEntity<Notification> markAsDelivered(@PathVariable Long id) {
        Notification notification = notificationService.markAsDelivered(id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/{id}/failed")
    public ResponseEntity<Notification> markAsFailed(@PathVariable Long id) {
        Notification notification = notificationService.markAsFailed(id);
        return ResponseEntity.ok(notification);
    }
}
