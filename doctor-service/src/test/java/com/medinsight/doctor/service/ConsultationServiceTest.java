package com.medinsight.doctor.service;

import com.medinsight.commons.exception.ResourceNotFoundException;
import com.medinsight.doctor.entity.Consultation;
import com.medinsight.doctor.repository.ConsultationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Consultation Service Tests")
class ConsultationServiceTest {

    @Mock
    private ConsultationRepository consultationRepository;

    @InjectMocks
    private ConsultationService consultationService;

    private Consultation testConsultation;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        testConsultation = Consultation.builder()
                .id(1L)
                .appointmentId(500L)
                .patientId(100L)
                .doctorId(200L)
                .consultationDate(now)
                .chiefComplaint("Headache")
                .diagnosis("Migraine")
                .prescription("Paracetamol")
                .notes("Rest advised")
                .followUpInstructions("Come back in 1 week")
                .visibleInPortal(true)
                .build();
    }

    @Test
    @DisplayName("Should create consultation successfully")
    void shouldCreateConsultation() {
        when(consultationRepository.save(any(Consultation.class))).thenReturn(testConsultation);

        Consultation result = consultationService.createConsultation(testConsultation);

        assertThat(result).isNotNull();
        assertThat(result.getDiagnosis()).isEqualTo("Migraine");

        verify(consultationRepository, times(1)).save(any(Consultation.class));
    }

    @Test
    @DisplayName("Should get consultation by ID successfully")
    void shouldGetConsultationById() {
        when(consultationRepository.findById(1L)).thenReturn(Optional.of(testConsultation));

        Consultation result = consultationService.getConsultationById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(consultationRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when consultation not found by ID")
    void shouldThrowExceptionWhenConsultationNotFoundById() {
        when(consultationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultationService.getConsultationById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Consultation");

        verify(consultationRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should get consultation by appointment ID")
    void shouldGetConsultationByAppointmentId() {
        when(consultationRepository.findByAppointmentId(500L)).thenReturn(Optional.of(testConsultation));

        Consultation result = consultationService.getConsultationByAppointmentId(500L);

        assertThat(result).isNotNull();
        assertThat(result.getAppointmentId()).isEqualTo(500L);

        verify(consultationRepository, times(1)).findByAppointmentId(500L);
    }

    @Test
    @DisplayName("Should throw exception when consultation not found by appointment ID")
    void shouldThrowExceptionWhenConsultationNotFoundByAppointmentId() {
        when(consultationRepository.findByAppointmentId(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> consultationService.getConsultationByAppointmentId(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Consultation");

        verify(consultationRepository, times(1)).findByAppointmentId(999L);
    }

    @Test
    @DisplayName("Should get consultations by patient ID")
    void shouldGetConsultationsByPatientId() {
        when(consultationRepository.findByPatientId(100L)).thenReturn(Collections.singletonList(testConsultation));

        List<Consultation> results = consultationService.getConsultationsByPatientId(100L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPatientId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Should get consultations by doctor ID")
    void shouldGetConsultationsByDoctorId() {
        when(consultationRepository.findByDoctorId(200L)).thenReturn(Collections.singletonList(testConsultation));

        List<Consultation> results = consultationService.getConsultationsByDoctorId(200L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getDoctorId()).isEqualTo(200L);
    }

    @Test
    @DisplayName("Should get patient portal consultations")
    void shouldGetPatientPortalConsultations() {
        when(consultationRepository.findByPatientIdAndVisibleInPortal(100L, true))
                .thenReturn(Collections.singletonList(testConsultation));

        List<Consultation> results = consultationService.getPatientPortalConsultations(100L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).isVisibleInPortal()).isTrue();
    }

    @Test
    @DisplayName("Should update consultation successfully")
    void shouldUpdateConsultation() {
        Consultation updateDetails = Consultation.builder()
                .chiefComplaint("Severe Headache")
                .diagnosis("Severe Migraine")
                .prescription("Stronger meds")
                .notes("New notes")
                .followUpInstructions("Check in 2 days")
                .visibleInPortal(false)
                .build();

        when(consultationRepository.findById(1L)).thenReturn(Optional.of(testConsultation));
        when(consultationRepository.save(any(Consultation.class))).thenReturn(testConsultation);

        Consultation result = consultationService.updateConsultation(1L, updateDetails);

        assertThat(result).isNotNull();
        // Check local object change
        assertThat(testConsultation.getDiagnosis()).isEqualTo("Severe Migraine");
        assertThat(testConsultation.isVisibleInPortal()).isFalse();

        verify(consultationRepository, times(1)).findById(1L);
        verify(consultationRepository, times(1)).save(testConsultation);
    }
}
