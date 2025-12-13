package com.medinsight.analytics.document;

import com.medinsight.analytics.enums.AnalysisType;
import com.medinsight.analytics.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "analysis_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisRequest {

    @Id
    private String id;

    private Long patientId;

    private AnalysisType analysisType;

    private String inputData;

    private RequestStatus status;

    private LocalDateTime requestedAt;

    private LocalDateTime completedAt;

    private String errorMessage;
}
