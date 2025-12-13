package com.medinsight.patient.repository;

import com.medinsight.patient.entity.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {
    List<MedicalHistory> findByPatientId(Long patientId);

    List<MedicalHistory> findByPatientIdAndVisibleInPortal(Long patientId, boolean visibleInPortal);
}
