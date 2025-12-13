package com.medinsight.patient.service;

import com.medinsight.patient.entity.MedicalRecord;
import com.medinsight.patient.repository.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;

    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordRepository.save(medicalRecord);
    }

    public Optional<MedicalRecord> getMedicalRecordByPatientId(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId);
    }

    public MedicalRecord updateMedicalRecord(Long id, MedicalRecord recordDetails) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found with id: " + id));

        record.setBloodGroup(recordDetails.getBloodGroup());
        record.setAllergies(recordDetails.getAllergies());
        record.setChronicConditions(recordDetails.getChronicConditions());
        record.setCurrentMedications(recordDetails.getCurrentMedications());
        record.setGeneralNotes(recordDetails.getGeneralNotes());
        record.setVisibleInPortal(recordDetails.isVisibleInPortal());

        return medicalRecordRepository.save(record);
    }
}
