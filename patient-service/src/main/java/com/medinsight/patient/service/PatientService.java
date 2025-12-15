package com.medinsight.patient.service;

import com.medinsight.patient.entity.Patient;
import com.medinsight.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final EventPublisher eventPublisher;

    public Patient createPatient(Patient patient) {
        Patient savedPatient = patientRepository.save(patient);

        // Publish audit event
        eventPublisher.publishAuditEvent(
                savedPatient.getUserId(),
                savedPatient.getEmail(),
                "CREATE",
                "Patient",
                savedPatient.getId().toString(),
                null, // ipAddress - should be passed from controller
                null, // userAgent - should be passed from controller
                Map.of("firstName", savedPatient.getFirstName(),
                        "lastName", savedPatient.getLastName(),
                        "email", savedPatient.getEmail()));

        // Publish notification event
        eventPublisher.publishNotificationEvent(
                savedPatient.getUserId(),
                "PATIENT_WELCOME",
                "EMAIL",
                "Welcome to MedInsight",
                String.format("Dear %s %s, welcome to MedInsight E-Health System!",
                        savedPatient.getFirstName(), savedPatient.getLastName()));

        return savedPatient;
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Optional<Patient> getPatientByUserId(UUID userId) {
        return patientRepository.findByUserId(userId);
    }

    public Optional<Patient> getPatientByEmail(String email) {
        return patientRepository.findByEmail(email);
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + id));

        patient.setFirstName(patientDetails.getFirstName());
        patient.setLastName(patientDetails.getLastName());
        patient.setEmail(patientDetails.getEmail());
        patient.setPhoneNumber(patientDetails.getPhoneNumber());
        patient.setDateOfBirth(patientDetails.getDateOfBirth());
        patient.setGender(patientDetails.getGender());
        patient.setBloodType(patientDetails.getBloodType());
        patient.setMedicalHistorySummary(patientDetails.getMedicalHistorySummary());
        patient.setPortalActive(patientDetails.isPortalActive());

        Patient updatedPatient = patientRepository.save(patient);

        // Publish audit event
        eventPublisher.publishAuditEvent(
                updatedPatient.getUserId(),
                updatedPatient.getEmail(),
                "UPDATE",
                "Patient",
                updatedPatient.getId().toString(),
                null,
                null,
                Map.of("updatedFields", "firstName, lastName, email, etc."));

        return updatedPatient;
    }

    public void deletePatient(Long id) {
        Optional<Patient> patientOpt = patientRepository.findById(id);
        patientRepository.deleteById(id);

        // Publish audit event if patient existed
        patientOpt.ifPresent(patient -> eventPublisher.publishAuditEvent(
                patient.getUserId(),
                patient.getEmail(),
                "DELETE",
                "Patient",
                patient.getId().toString(),
                null,
                null,
                Map.of("deletedPatient", patient.getEmail())));
    }

    public boolean existsByEmail(String email) {
        return patientRepository.existsByEmail(email);
    }
}
