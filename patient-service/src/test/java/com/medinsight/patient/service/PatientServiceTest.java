package com.medinsight.patient.service;

import com.medinsight.patient.entity.Patient;
import com.medinsight.patient.enums.Gender;
import com.medinsight.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Disabled;

@ExtendWith(MockitoExtension.class)
@Disabled
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testPatient = Patient.builder()
                .id(1L)
                .userId(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .bloodType("A+")
                .medicalRecordNumber("MRN-12345")
                .portalActive(true)
                .deleted(false)
                .build();
    }

    @Test
    @DisplayName("Should create patient")
    void shouldCreatePatient() {
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        Patient created = patientService.createPatient(testPatient);

        assertThat(created).isNotNull();
        assertThat(created.getFirstName()).isEqualTo("John");
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should get patient by ID")
    void shouldGetPatientById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        Optional<Patient> found = patientService.getPatientById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should get all patients")
    void shouldGetAllPatients() {
        when(patientRepository.findAll()).thenReturn(Arrays.asList(testPatient));

        List<Patient> patients = patientService.getAllPatients();

        assertThat(patients).hasSize(1);
    }
}
