package com.medinsight.patient.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;

    @Column(columnDefinition = "text")
    private String medicalHistorySummary;

    private boolean deleted = false;
}
