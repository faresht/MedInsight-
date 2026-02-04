package com.medinsight.analytics.controller;

import com.medinsight.analytics.client.AIServiceClient;
import com.medinsight.analytics.dto.breast.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

/**
 * REST Controller for Breast Cancer Diagnosis using AI Service.
 * Provides endpoints for multimodal breast cancer risk assessment.
 */
@RestController
@RequestMapping("/api/breast-cancer")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Breast Cancer Diagnosis", description = "AI-powered breast cancer diagnosis using multimodal data")
public class BreastCancerController {

    private final AIServiceClient aiServiceClient;

    /**
     * Perform comprehensive breast cancer diagnosis.
     *
     * @param request Diagnosis request with imaging, genomic, and/or clinical data
     * @return Diagnosis response with predictions and recommendations
     */
    @PostMapping("/diagnose")
    @PreAuthorize("hasAnyRole('MEDECIN', 'GESTIONNAIRE', 'ADMIN')")
    @Operation(summary = "Diagnose breast cancer", description = "Perform comprehensive breast cancer diagnosis using multimodal AI analysis")
    public Mono<ResponseEntity<BreastCancerDiagnosisResponse>> diagnose(
            @Valid @RequestBody BreastCancerDiagnosisRequest request) {

        log.info("Received breast cancer diagnosis request for patient: {}", request.getPatientId());

        return aiServiceClient.diagnose(request)
                .map(response -> {
                    log.info("Diagnosis completed for patient: {} - Risk Score: {}%",
                            response.getPatientId(),
                            response.getDiagnosisSummary().getRiskScore());
                    return ResponseEntity.ok(response);
                })
                .onErrorResume(IllegalArgumentException.class, ex -> {
                    log.error("Invalid diagnosis request: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("Diagnosis failed: {}", ex.getMessage(), ex);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }

    /**
     * Check AI service health status.
     *
     * @return Health status of the AI service
     */
    @GetMapping("/ai-service/health")
    @PreAuthorize("hasAnyRole('MEDECIN', 'GESTIONNAIRE', 'ADMIN', 'RESPONSABLE_SECURITE')")
    @Operation(summary = "Check AI service health", description = "Get health status of the AI service")
    public Mono<ResponseEntity<AIServiceHealthResponse>> checkAIServiceHealth() {
        log.info("Checking AI service health");

        return aiServiceClient.checkHealth()
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    log.error("AI service health check failed: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
                });
    }

    /**
     * List available AI agents and their capabilities.
     *
     * @return List of AI agents
     */
    @GetMapping("/ai-service/agents")
    @PreAuthorize("hasAnyRole('MEDECIN', 'GESTIONNAIRE', 'ADMIN')")
    @Operation(summary = "List AI agents", description = "Get list of available AI agents and their capabilities")
    public Mono<ResponseEntity<AgentListResponse>> listAgents() {
        log.info("Fetching AI agents list");

        return aiServiceClient.listAgents()
                .map(ResponseEntity::ok)
                .onErrorResume(ex -> {
                    log.error("Failed to fetch agents list: {}", ex.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
                });
    }
}
