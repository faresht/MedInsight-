package com.medinsight.report.controller;

import com.medinsight.report.entity.MedicalReport;
import com.medinsight.report.enums.ReportType;
import com.medinsight.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<MedicalReport> createReport(@RequestBody MedicalReport report) {
        MedicalReport created = reportService.createReport(report);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalReport> getReportById(@PathVariable Long id) {
        MedicalReport report = reportService.getReportById(id);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MedicalReport>> getReportsByPatientId(@PathVariable Long patientId) {
        List<MedicalReport> reports = reportService.getReportsByPatientId(patientId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<MedicalReport>> getReportsByDoctorId(@PathVariable Long doctorId) {
        List<MedicalReport> reports = reportService.getReportsByDoctorId(doctorId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/patient/{patientId}/type/{reportType}")
    public ResponseEntity<List<MedicalReport>> getReportsByPatientIdAndType(
            @PathVariable Long patientId,
            @PathVariable ReportType reportType) {
        List<MedicalReport> reports = reportService.getReportsByPatientIdAndType(patientId, reportType);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/patient/{patientId}/portal")
    public ResponseEntity<List<MedicalReport>> getPatientPortalReports(@PathVariable Long patientId) {
        List<MedicalReport> reports = reportService.getPatientPortalReports(patientId);
        return ResponseEntity.ok(reports);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalReport> updateReport(@PathVariable Long id, @RequestBody MedicalReport report) {
        MedicalReport updated = reportService.updateReport(id, report);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
