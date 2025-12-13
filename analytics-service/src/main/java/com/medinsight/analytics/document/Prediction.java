package com.medinsight.analytics.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "predictions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Prediction {

    @Id
    private String id;

    private String analysisRequestId;

    private Long patientId;

    private Double confidenceScore;

    private String predictionLabel;

    private String explanation;

    private Map<String, Object> metrics;

    private LocalDateTime createdAt;
}
