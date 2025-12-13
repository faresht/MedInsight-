package com.medinsight.audit.controller;

import com.medinsight.audit.document.AuditLog;
import com.medinsight.audit.enums.AuditAction;
import com.medinsight.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByUserId(@PathVariable UUID userId) {
        List<AuditLog> logs = auditService.getAuditLogsByUserId(userId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByAction(@PathVariable AuditAction action) {
        List<AuditLog> logs = auditService.getAuditLogsByAction(action);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/entity/{entityType}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByEntityType(@PathVariable String entityType) {
        List<AuditLog> logs = auditService.getAuditLogsByEntityType(entityType);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/range")
    public ResponseEntity<List<AuditLog>> getAuditLogsInDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        List<AuditLog> logs = auditService.getAuditLogsInDateRange(start, end);
        return ResponseEntity.ok(logs);
    }
}
