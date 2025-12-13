package com.medinsight.analytics.repository;

import com.medinsight.analytics.document.AnalysisRequest;
import com.medinsight.analytics.enums.RequestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRequestRepository extends MongoRepository<AnalysisRequest, String> {
    List<AnalysisRequest> findByPatientId(Long patientId);

    List<AnalysisRequest> findByStatus(RequestStatus status);
}
