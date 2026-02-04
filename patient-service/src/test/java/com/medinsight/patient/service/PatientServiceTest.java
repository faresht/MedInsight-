package com.medinsight.patient.service;

import com.medinsight.patient.dto.PatientDTO;
import com.medinsight.patient.entity.Patient;
import com.medinsight.patient.enums.BloodType;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Patient Service Tests")
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient testPatient;
    private PatientDTO testPatientDTO;

    @BeforeEach
    void setUp() {
        testPatient = Patient.builder()
                .id(1L)
                .userId("user-123")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .bloodType(BloodType.A_POSITIVE)
                .address("123 Main St")
                .deleted(false)
                .build();

        testPatientDTO = PatientDTO.builder()
                .userId("user-123")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .bloodType(BloodType.A_POSITIVE)
                .address("123 Main St")
                .build();
    }

    @Test
    @DisplayName("Should create patient successfully")
    void shouldCreatePatient() {
        when(patientRepository.existsByEmail(anyString())).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        PatientDTO result = patientService.createPatient(testPatientDTO);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("John");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");

        verify(patientRepository, times(1)).existsByEmail("john.doe@example.com");
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        when(patientRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> patientService.createPatient(testPatientDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Email already exists");

        verify(patientRepository, times(1)).existsByEmail("john.doe@example.com");
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should get patient by ID successfully")
    void shouldGetPatientById() {
        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testPatient));

        Optional<PatientDTO> result = patientService.getPatientById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("John");
        assertThat(result.get().getEmail()).isEqualTo("john.doe@example.com");

        verify(patientRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    @DisplayName("Should return empty when patient not found")
    void shouldReturnEmptyWhenPatientNotFound() {
        when(patientRepository.findByIdAndDeletedFalse(999L)).thenReturn(Optional.empty());

        Optional<PatientDTO> result = patientService.getPatientById(999L);

        assertThat(result).isEmpty();
        verify(patientRepository, times(1)).findByIdAndDeletedFalse(999L);
    }

    @Test
    @DisplayName("Should get all patients successfully")
    void shouldGetAllPatients() {
        Patient patient2 = Patient.builder()
                .id(2L)
                .userId("user-456")
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .deleted(false)
                .build();

        when(patientRepository.findAllByDeletedFalse()).thenReturn(Arrays.asList(testPatient, patient2));

        List<PatientDTO> results = patientService.getAllPatients();

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getFirstName()).isEqualTo("John");
        assertThat(results.get(1).getFirstName()).isEqualTo("Jane");

        verify(patientRepository, times(1)).findAllByDeletedFalse();
    }

    @Test
    @DisplayName("Should update patient successfully")
    void shouldUpdatePatient() {
        PatientDTO updateDTO = PatientDTO.builder()
                .firstName("John")
                .lastName("Doe Updated")
                .email("john.updated@example.com")
                .phone("+9876543210")
                .build();

        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        PatientDTO result = patientService.updatePatient(1L, updateDTO);

        assertThat(result).isNotNull();
        verify(patientRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent patient")
    void shouldThrowExceptionWhenUpdatingNonExistentPatient() {
        when(patientRepository.findByIdAndDeletedFalse(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.updatePatient(999L, testPatientDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Patient not found");

        verify(patientRepository, times(1)).findByIdAndDeletedFalse(999L);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should soft delete patient successfully")
    void shouldSoftDeletePatient() {
        when(patientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        patientService.deletePatient(1L);

        verify(patientRepository, times(1)).findByIdAndDeletedFalse(1L);
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    @DisplayName("Should check if email exists")
    void shouldCheckEmailExists() {
        when(patientRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        boolean exists = patientService.existsByEmail("john.doe@example.com");

        assertThat(exists).isTrue();
        verify(patientRepository, times(1)).existsByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Should get patient by user ID")
    void shouldGetPatientByUserId() {
        when(patientRepository.findByUserIdAndDeletedFalse("user-123")).thenReturn(Optional.of(testPatient));

        Optional<PatientDTO> result = patientService.getPatientByUserId("user-123");

        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo("user-123");
        verify(patientRepository, times(1)).findByUserIdAndDeletedFalse("user-123");
    }
}
