package com.medinsight.audit.repository;

import com.medinsight.audit.document.SecurityAlert;
import com.medinsight.audit.enums.AlertSeverity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecurityAlertRepository extends MongoRepository<SecurityAlert, String> {
    List<SecurityAlert> findBySeverity(AlertSeverity severity);

    List<SecurityAlert> findByResolved(boolean resolved);

    List<SecurityAlert> findBySeverityAndResolved(AlertSeverity severity, boolean resolved);
}
