package com.medinsight.doctor.service;

import com.medinsight.doctor.entity.Appointment;
import com.medinsight.doctor.enums.AppointmentStatus;
import com.medinsight.doctor.enums.AppointmentType;
import com.medinsight.doctor.repository.AppointmentRepository;
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
@DisplayName("Appointment Service Tests")
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment testAppointment;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        testAppointment = Appointment.builder()
                .id(1L)
                .patientId(100L)
                .doctorId(200L)
                .appointmentDateTime(now)
                .durationMinutes(30)
                .type(AppointmentType.CONSULTATION)
                .status(AppointmentStatus.SCHEDULED)
                .reason("Regular Checkup")
                .notes("Some notes")
                .build();
    }

    @Test
    @DisplayName("Should create appointment successfully")
    void shouldCreateAppointment() {
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        Appointment result = appointmentService.createAppointment(testAppointment);

        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED); // Should be set to SCHEDULED by service

        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should get appointment by ID successfully")
    void shouldGetAppointmentById() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));

        Optional<Appointment> result = appointmentService.getAppointmentById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);

        verify(appointmentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should get appointments by patient ID")
    void shouldGetAppointmentsByPatientId() {
        when(appointmentRepository.findByPatientId(100L)).thenReturn(Collections.singletonList(testAppointment));

        List<Appointment> results = appointmentService.getAppointmentsByPatientId(100L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getPatientId()).isEqualTo(100L);

        verify(appointmentRepository, times(1)).findByPatientId(100L);
    }

    @Test
    @DisplayName("Should get appointments by doctor ID")
    void shouldGetAppointmentsByDoctorId() {
        when(appointmentRepository.findByDoctorId(200L)).thenReturn(Collections.singletonList(testAppointment));

        List<Appointment> results = appointmentService.getAppointmentsByDoctorId(200L);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getDoctorId()).isEqualTo(200L);

        verify(appointmentRepository, times(1)).findByDoctorId(200L);
    }

    @Test
    @DisplayName("Should get doctor appointments for date range")
    void shouldGetDoctorAppointmentsForDateRange() {
        LocalDateTime start = now.minusDays(1);
        LocalDateTime end = now.plusDays(1);
        when(appointmentRepository.findByDoctorIdAndAppointmentDateTimeBetween(200L, start, end))
                .thenReturn(Collections.singletonList(testAppointment));

        List<Appointment> results = appointmentService.getDoctorAppointmentsForDateRange(200L, start, end);

        assertThat(results).hasSize(1);
        verify(appointmentRepository, times(1)).findByDoctorIdAndAppointmentDateTimeBetween(200L, start, end);
    }

    @Test
    @DisplayName("Should update appointment status")
    void shouldUpdateAppointmentStatus() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        Appointment result = appointmentService.updateAppointmentStatus(1L, AppointmentStatus.CONFIRMED);

        assertThat(result).isNotNull();
        // Check local object change since mock returns same object
        assertThat(testAppointment.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);

        verify(appointmentRepository, times(1)).findById(1L);
        verify(appointmentRepository, times(1)).save(testAppointment);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent appointment")
    void shouldThrowExceptionWhenUpdatingNonExistentAppointment() {
        when(appointmentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> appointmentService.updateAppointmentStatus(999L, AppointmentStatus.CONFIRMED))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Appointment not found");

        verify(appointmentRepository, times(1)).findById(999L);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should confirm appointment")
    void shouldConfirmAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        appointmentService.confirmAppointment(1L);

        assertThat(testAppointment.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
    }

    @Test
    @DisplayName("Should cancel appointment")
    void shouldCancelAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        appointmentService.cancelAppointment(1L);

        assertThat(testAppointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
    }

    @Test
    @DisplayName("Should complete appointment")
    void shouldCompleteAppointment() {
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(testAppointment));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(testAppointment);

        appointmentService.completeAppointment(1L);

        assertThat(testAppointment.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
    }
}
