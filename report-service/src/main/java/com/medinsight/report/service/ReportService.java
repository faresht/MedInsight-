package com.medinsight.report.service;

import com.medinsight.commons.exception.ResourceNotFoundException;
import com.medinsight.report.entity.MedicalReport;
import com.medinsight.report.enums.ReportType;
import com.medinsight.report.repository.MedicalReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final MedicalReportRepository medicalReportRepository;

    public MedicalReport createReport(MedicalReport report) {
        return medicalReportRepository.save(report);
    }

    public MedicalReport getReportById(Long id) {
        return medicalReportRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MedicalReport", "id", id));
    }

    public List<MedicalReport> getReportsByPatientId(Long patientId) {
        return medicalReportRepository.findByPatientId(patientId);
    }

    public List<MedicalReport> getReportsByDoctorId(Long doctorId) {
        return medicalReportRepository.findByDoctorId(doctorId);
    }

    public List<MedicalReport> getReportsByPatientIdAndType(Long patientId, ReportType reportType) {
        return medicalReportRepository.findByPatientIdAndReportType(patientId, reportType);
    }

    public List<MedicalReport> getPatientPortalReports(Long patientId) {
        return medicalReportRepository.findByPatientIdAndVisibleInPortal(patientId, true);
    }

    public MedicalReport updateReport(Long id, MedicalReport reportDetails) {
        MedicalReport report = getReportById(id);

        report.setTitle(reportDetails.getTitle());
        report.setContent(reportDetails.getContent());
        report.setPdfUrl(reportDetails.getPdfUrl());
        report.setVisibleInPortal(reportDetails.isVisibleInPortal());

        return medicalReportRepository.save(report);
    }

    public void deleteReport(Long id) {
        MedicalReport report = getReportById(id);
        medicalReportRepository.delete(report);
    }
}
