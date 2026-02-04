package com.medinsight.analytics.dto.breast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Response DTO for breast cancer diagnosis.
 * Contains comprehensive diagnosis results from all AI agents.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreastCancerDiagnosisResponse {

    private String patientId;
    private DiagnosisSummary diagnosisSummary;
    private Map<String, AgentAnalysis> agentAnalyses;
    private List<KeyFinding> keyFindings;
    private List<String> recommendations;
    private String interpretation;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiagnosisSummary {
        private String finalDiagnosis;
        private Double riskScore;
        private Double confidence;
        private String consensusLevel;
        private Double agentAgreementPercentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentAnalysis {
        private String agent;
        private Integer prediction;
        private Double confidence;
        private String confidenceLevel;
        private Map<String, Object> features;
        private String explanation;
        private String modality; // For imaging agent
        private List<String> riskGenes; // For genomic agent
        private List<String> riskFactors; // For clinical agent
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyFinding {
        private String agent;
        private String finding;
    }
}
