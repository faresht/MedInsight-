package com.medinsight.analytics.client;

import com.medinsight.analytics.dto.breast.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * HTTP client for communicating with the Python AI Service.
 * Handles breast cancer diagnosis requests using multimodal data.
 */
@Component
public class AIServiceClient {

    private final WebClient webClient;

    public AIServiceClient(@Value("${ai.service.url:http://localhost:8088}") String aiServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(aiServiceUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Perform breast cancer diagnosis using the AI service.
     *
     * @param request Diagnosis request with imaging, genomic, and/or clinical data
     * @return Diagnosis response with predictions and recommendations
     */
    public Mono<BreastCancerDiagnosisResponse> diagnose(BreastCancerDiagnosisRequest request) {
        return webClient.post()
                .uri("/api/v1/diagnose")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(BreastCancerDiagnosisResponse.class)
                .timeout(Duration.ofSeconds(30))
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new IllegalArgumentException(
                                "Invalid diagnosis request: " + ex.getResponseBodyAsString()));
                    } else if (ex.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
                        return Mono.error(new RuntimeException("AI service error: " + ex.getResponseBodyAsString()));
                    }
                    return Mono.error(ex);
                });
    }

    /**
     * Check health status of the AI service.
     *
     * @return Health status response
     */
    public Mono<AIServiceHealthResponse> checkHealth() {
        return webClient.get()
                .uri("/health")
                .retrieve()
                .bodyToMono(AIServiceHealthResponse.class)
                .timeout(Duration.ofSeconds(5));
    }

    /**
     * Get list of available AI agents and their capabilities.
     *
     * @return Agent information
     */
    public Mono<AgentListResponse> listAgents() {
        return webClient.get()
                .uri("/api/v1/agents")
                .retrieve()
                .bodyToMono(AgentListResponse.class)
                .timeout(Duration.ofSeconds(5));
    }
}
