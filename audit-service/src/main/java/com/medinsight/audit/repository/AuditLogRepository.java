package com.medinsight.audit.repository;

import com.medinsight.audit.document.AuditLog;
import com.medinsight.audit.enums.AuditAction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
    List<AuditLog> findByUserId(UUID userId);

    List<AuditLog> findByAction(AuditAction action);

    List<AuditLog> findByEntityType(String entityType);

    List<AuditLog> findByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
