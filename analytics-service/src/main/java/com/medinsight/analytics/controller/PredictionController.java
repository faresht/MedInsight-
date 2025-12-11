package com.medinsight.analytics.controller;

import com.medinsight.analytics.service.PredictionService;
import com.medinsight.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping("/predict/relapse-risk")
    @PreAuthorize("hasRole('doctor')")
    public ResponseEntity<ApiResponse<String>> predictRelapseRisk(@RequestBody Map<String, String> request) {
        String result = predictionService.predictRelapseRisk(request.get("history"));
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @PostMapping("/predict/bed-occupancy")
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<ApiResponse<String>> predictBedOccupancy(@RequestBody Map<String, String> request) {
        String result = predictionService.predictBedOccupancy(request.get("data"));
        return ResponseEntity.ok(ApiResponse.success(result));
    }
    
    @PostMapping("/detect-anomalies")
    @PreAuthorize("hasRole('admin') or hasRole('sec-officer')")
    public ResponseEntity<ApiResponse<String>> detectAnomalies(@RequestBody Map<String, String> request) {
        String result = predictionService.detectAnomalies(request.get("logs"));
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
