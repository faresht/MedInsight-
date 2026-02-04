package com.medinsight.patient.listener;

import com.medinsight.patient.entity.Patient;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class DataIntegrityListener {

    @PrePersist
    @PreUpdate
    public void computeHash(Patient patient) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String dataToHash = patient.getFirstName() + "|" +
                    patient.getLastName() + "|" +
                    patient.getEmail() + "|" +
                    patient.getMedicalRecordNumber();

            byte[] encodedhash = digest.digest(dataToHash.getBytes(StandardCharsets.UTF_8));
            String hash = Base64.getEncoder().encodeToString(encodedhash);

            patient.setDataHash(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to compute data integrity hash", e);
        }
    }
}
