package com.medinsight.doctor.controller;

import com.medinsight.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<String>> status() {
        return ResponseEntity.ok(ApiResponse.success("Doctor Service Operational"));
    }
}
