package com.medinsight.auth.controller;

import com.medinsight.auth.service.AuthService;
import com.medinsight.common.dto.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final com.medinsight.commons.client.NotificationClient notificationClient;

    public AuthController(AuthService authService,
            com.medinsight.commons.client.NotificationClient notificationClient) {
        this.authService = authService;
        this.notificationClient = notificationClient;
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<String>> createUser(
            @RequestBody com.medinsight.auth.dto.RegisterRequest request) {
        ResponseEntity<ApiResponse<String>> response = ResponseEntity.ok(authService.createUser(request));
        try {
            com.medinsight.commons.dto.NotificationDTO notification = com.medinsight.commons.dto.NotificationDTO
                    .builder()
                    .recipientUserId(request.getUsername()) // Using username/email as ID for now or fetch new ID
                    .recipientEmail(request.getEmail())
                    .subject("Welcome to MedInsight+")
                    .message("Welcome " + request.getFirstName() + "! Your account has been created successfully.")
                    .type("EMAIL")
                    .channel("EMAIL")
                    .build();
            notificationClient.sendNotification(notification);
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
        return response;
    }
}
