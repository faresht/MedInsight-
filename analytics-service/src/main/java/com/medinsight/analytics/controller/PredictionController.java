package com.medinsight.analytics.controller;

import com.medinsight.analytics.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;

    @PostMapping("/predict/relapse-risk")
    public ResponseEntity<String> predictRelapseRisk(@RequestBody String patientHistory) {
        String prediction = predictionService.predictRelapseRisk(patientHistory);
        return ResponseEntity.ok(prediction);
    }

    @PostMapping("/predict/bed-occupancy")
    public ResponseEntity<String> predictBedOccupancy(@RequestBody String data) {
        String prediction = predictionService.predictBedOccupancy(data);
        return ResponseEntity.ok(prediction);
    }

    @PostMapping("/detect/anomalies")
    public ResponseEntity<String> detectAnomalies(@RequestBody String logData) {
        String analysis = predictionService.detectAnomalies(logData);
        return ResponseEntity.ok(analysis);
    }
}
