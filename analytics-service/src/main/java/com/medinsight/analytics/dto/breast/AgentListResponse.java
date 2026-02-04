package com.medinsight.analytics.dto.breast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response containing list of available AI agents.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentListResponse {
    private List<AgentInfo> agents;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AgentInfo {
        private String name;
        private String description;
        private List<String> supportedModalities;
        private List<String> monitoredGenes;
        private List<String> analyzedFactors;
        private Double confidenceThreshold;
        private String fusionMethod;
        private String architecture;
    }
}
