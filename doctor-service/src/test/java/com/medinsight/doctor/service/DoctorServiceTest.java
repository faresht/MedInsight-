package com.medinsight.doctor.service;

import com.medinsight.doctor.entity.Doctor;
import com.medinsight.doctor.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Doctor Service Tests")
class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorService doctorService;

    private Doctor testDoctor;
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        testDoctor = Doctor.builder()
                .id(1L)
                .userId(userId)
                .firstName("Gregory")
                .lastName("House")
                .email("g.house@medinsight.com")
                .specialization("DIAGNOSTIC_MEDICINE")
                .licenseNumber("MD12345")
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should create doctor successfully")
    void shouldCreateDoctor() {
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        Doctor result = doctorService.createDoctor(testDoctor);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Gregory");
        assertThat(result.getLicenseNumber()).isEqualTo("MD12345");

        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should get doctor by ID successfully")
    void shouldGetDoctorById() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));

        Optional<Doctor> result = doctorService.getDoctorById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getEmail()).isEqualTo("g.house@medinsight.com");

        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when doctor not found by ID")
    void shouldReturnEmptyWhenDoctorNotFoundById() {
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Doctor> result = doctorService.getDoctorById(999L);

        assertThat(result).isEmpty();
        verify(doctorRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get doctor by User ID successfully")
    void shouldGetDoctorByUserId() {
        when(doctorRepository.findByUserId(userId)).thenReturn(Optional.of(testDoctor));

        Optional<Doctor> result = doctorService.getDoctorByUserId(userId);

        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(userId);

        verify(doctorRepository, times(1)).findByUserId(userId);
    }

    @Test
    @DisplayName("Should get all active doctors")
    void shouldGetAllActiveDoctors() {
        Doctor doctor2 = Doctor.builder()
                .id(2L)
                .firstName("James")
                .lastName("Wilson")
                .active(true)
                .build();

        when(doctorRepository.findByActive(true)).thenReturn(Arrays.asList(testDoctor, doctor2));

        List<Doctor> results = doctorService.getAllActiveDoctors();

        assertThat(results).hasSize(2);
        assertThat(results.get(0).getFirstName()).isEqualTo("Gregory");
        assertThat(results.get(1).getFirstName()).isEqualTo("James");

        verify(doctorRepository, times(1)).findByActive(true);
    }

    @Test
    @DisplayName("Should get doctors by specialization")
    void shouldGetDoctorsBySpecialization() {
        when(doctorRepository.findBySpecialization("DIAGNOSTIC_MEDICINE")).thenReturn(Arrays.asList(testDoctor));

        List<Doctor> results = doctorService.getDoctorsBySpecialization("DIAGNOSTIC_MEDICINE");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getSpecialization()).isEqualTo("DIAGNOSTIC_MEDICINE");

        verify(doctorRepository, times(1)).findBySpecialization("DIAGNOSTIC_MEDICINE");
    }

    @Test
    @DisplayName("Should update doctor successfully")
    void shouldUpdateDoctor() {
        Doctor updateDetails = Doctor.builder()
                .firstName("Gregory Updated")
                .lastName("House")
                .email("g.house.updated@medinsight.com")
                .phoneNumber("+199999999")
                .specialization("DIAGNOSTIC_MEDICINE")
                .qualification("MD, PhD")
                .experienceYears(20)
                .photoUrl("http://new-photo.url")
                .active(true)
                .build();

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        Doctor result = doctorService.updateDoctor(1L, updateDetails);

        assertThat(result).isNotNull();
        // Since we are mocking save to return testDoctor (which has "Gregory"), actual
        // updated object fields
        // would technically be what's passed to save. But in unit test with mocks, we
        // verify behavior.
        // In a real integration test, we'd check the saved entity.
        // Here we verify the setter calls happened on the retrieved entity (testDoctor)
        assertThat(testDoctor.getFirstName()).isEqualTo("Gregory Updated");
        assertThat(testDoctor.getEmail()).isEqualTo("g.house.updated@medinsight.com");

        verify(doctorRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).save(testDoctor);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent doctor")
    void shouldThrowExceptionWhenUpdatingNonExistentDoctor() {
        when(doctorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.updateDoctor(999L, testDoctor))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Doctor not found with id: 999");

        verify(doctorRepository, times(1)).findById(999L);
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    @DisplayName("Should deactivate doctor successfully")
    void shouldDeactivateDoctor() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(testDoctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(testDoctor);

        doctorService.deactivateDoctor(1L);

        assertThat(testDoctor.isActive()).isFalse();

        verify(doctorRepository, times(1)).findById(1L);
        verify(doctorRepository, times(1)).save(testDoctor);
    }
}
