package com.medinsight.audit.controller;

import com.medinsight.audit.document.AuditLog;
import com.medinsight.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller for managing Dead Letter Queue messages
 */
@Slf4j
@RestController
@RequestMapping("/api/dlq")
@RequiredArgsConstructor
public class DLQController {

    private final AuditLogRepository auditLogRepository;

    /**
     * Get statistics about DLQ messages
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDLQStats() {
        // In a real implementation, you would query DLQ topics
        // For now, return basic stats
        long totalAuditLogs = auditLogRepository.count();

        return ResponseEntity.ok(Map.of(
                "totalAuditLogs", totalAuditLogs,
                "dlqTopics", List.of("audit-events-dlq", "notification-events-dlq"),
                "status", "DLQ monitoring active"));
    }

    /**
     * Health check for DLQ monitoring
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "DLQ Monitoring"));
    }
}
