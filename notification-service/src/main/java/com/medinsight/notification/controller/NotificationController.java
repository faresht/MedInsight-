package com.medinsight.notification.controller;

import com.medinsight.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> status() {
        return ResponseEntity.ok(ApiResponse.success("Notification Service Operational"));
    }
}
