package com.medinsight.patient.repository;

import com.medinsight.patient.entity.MedicalDocument;
import com.medinsight.patient.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalDocumentRepository extends JpaRepository<MedicalDocument, Long> {
    List<MedicalDocument> findByPatientId(Long patientId);

    List<MedicalDocument> findByPatientIdAndDocumentType(Long patientId, DocumentType documentType);

    List<MedicalDocument> findByPatientIdAndVisibleInPortal(Long patientId, boolean visibleInPortal);
}
