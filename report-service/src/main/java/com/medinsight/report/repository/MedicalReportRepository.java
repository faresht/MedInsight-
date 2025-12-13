package com.medinsight.report.repository;

import com.medinsight.report.entity.MedicalReport;
import com.medinsight.report.enums.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalReportRepository extends JpaRepository<MedicalReport, Long> {
    List<MedicalReport> findByPatientId(Long patientId);

    List<MedicalReport> findByDoctorId(Long doctorId);

    List<MedicalReport> findByPatientIdAndReportType(Long patientId, ReportType reportType);

    List<MedicalReport> findByPatientIdAndVisibleInPortal(Long patientId, boolean visibleInPortal);
}
