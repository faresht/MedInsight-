package com.medinsight.analytics.repository;

import com.medinsight.analytics.document.Prediction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepository extends MongoRepository<Prediction, String> {
    List<Prediction> findByPatientId(Long patientId);

    List<Prediction> findByAnalysisRequestId(String analysisRequestId);
}
