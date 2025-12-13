package com.medinsight.patient.dto;

import com.medinsight.patient.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {
    private Long id;
    private UUID userId;
    private String medicalRecordNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String bloodType;
    private String medicalHistorySummary;
    private boolean portalActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
