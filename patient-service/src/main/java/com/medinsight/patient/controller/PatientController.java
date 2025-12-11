package com.medinsight.patient.controller;

import com.medinsight.common.dto.ApiResponse;
import com.medinsight.patient.entity.Patient;
import com.medinsight.patient.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    @PreAuthorize("hasRole('doctor') or hasRole('manager')")
    public ResponseEntity<ApiResponse<List<Patient>>> getAllPatients() {
        return ResponseEntity.ok(ApiResponse.success(patientService.getAllPatients()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('doctor') or hasRole('manager')")
    public ResponseEntity<ApiResponse<Patient>> getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id)
                .map(patient -> ResponseEntity.ok(ApiResponse.success(patient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('doctor') or hasRole('manager')")
    public ResponseEntity<ApiResponse<Patient>> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.ok(ApiResponse.success(patientService.createPatient(patient)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('doctor')")
    public ResponseEntity<ApiResponse<Patient>> updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return ResponseEntity.ok(ApiResponse.success(patientService.updatePatient(id, patient)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('manager')")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
