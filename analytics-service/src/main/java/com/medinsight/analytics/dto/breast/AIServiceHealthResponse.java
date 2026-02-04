package com.medinsight.analytics.dto.breast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Health response from AI service.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIServiceHealthResponse {
    private String status;
    private String service;
    private String version;
    private List<String> agentsLoaded;
}
