package com.medinsight.doctor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medinsight.doctor.entity.Appointment;
import com.medinsight.doctor.enums.AppointmentStatus;
import com.medinsight.doctor.enums.AppointmentType;
import com.medinsight.doctor.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@DisplayName("Appointment Controller Tests")
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    @WithMockUser(roles = "PATIENT")
    void shouldCreateAppointment() throws Exception {
        when(appointmentService.createAppointment(any(Appointment.class))).thenReturn(testAppointment);

        mockMvc.perform(post("/api/appointments")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAppointment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.patientId").value(100));

        verify(appointmentService, times(1)).createAppointment(any(Appointment.class));
    }

    @Test
    @DisplayName("Should get appointment by ID successfully")
    @WithMockUser(roles = "PATIENT")
    void shouldGetAppointmentById() throws Exception {
        when(appointmentService.getAppointmentById(1L)).thenReturn(Optional.of(testAppointment));

        mockMvc.perform(get("/api/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(appointmentService, times(1)).getAppointmentById(1L);
    }

    @Test
    @DisplayName("Should return 404 when appointment not found")
    @WithMockUser(roles = "PATIENT")
    void shouldReturn404WhenAppointmentNotFound() throws Exception {
        when(appointmentService.getAppointmentById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/appointments/999"))
                .andExpect(status().isNotFound());

        verify(appointmentService, times(1)).getAppointmentById(999L);
    }

    @Test
    @DisplayName("Should get appointments by patient ID")
    @WithMockUser(roles = "PATIENT")
    void shouldGetAppointmentsByPatientId() throws Exception {
        when(appointmentService.getAppointmentsByPatientId(100L))
                .thenReturn(Collections.singletonList(testAppointment));

        mockMvc.perform(get("/api/appointments/patient/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].patientId").value(100));

        verify(appointmentService, times(1)).getAppointmentsByPatientId(100L);
    }

    @Test
    @DisplayName("Should get appointments by doctor ID")
    @WithMockUser(roles = "DOCTOR")
    void shouldGetAppointmentsByDoctorId() throws Exception {
        when(appointmentService.getAppointmentsByDoctorId(200L)).thenReturn(Collections.singletonList(testAppointment));

        mockMvc.perform(get("/api/appointments/doctor/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].doctorId").value(200));

        verify(appointmentService, times(1)).getAppointmentsByDoctorId(200L);
    }

    @Test
    @DisplayName("Should update appointment status")
    @WithMockUser(roles = "DOCTOR")
    void shouldUpdateAppointmentStatus() throws Exception {
        testAppointment.setStatus(AppointmentStatus.CONFIRMED);
        when(appointmentService.updateAppointmentStatus(eq(1L), eq(AppointmentStatus.CONFIRMED)))
                .thenReturn(testAppointment);

        mockMvc.perform(put("/api/appointments/1/status")
                .with(csrf())
                .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(appointmentService, times(1)).updateAppointmentStatus(1L, AppointmentStatus.CONFIRMED);
    }

    @Test
    @DisplayName("Should confirm appointment")
    @WithMockUser(roles = "DOCTOR")
    void shouldConfirmAppointment() throws Exception {
        testAppointment.setStatus(AppointmentStatus.CONFIRMED);
        when(appointmentService.confirmAppointment(1L)).thenReturn(testAppointment);

        mockMvc.perform(put("/api/appointments/1/confirm")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(appointmentService, times(1)).confirmAppointment(1L);
    }

    @Test
    @DisplayName("Should cancel appointment")
    @WithMockUser(roles = "PATIENT")
    void shouldCancelAppointment() throws Exception {
        testAppointment.setStatus(AppointmentStatus.CANCELLED);
        when(appointmentService.cancelAppointment(1L)).thenReturn(testAppointment);

        mockMvc.perform(put("/api/appointments/1/cancel")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        verify(appointmentService, times(1)).cancelAppointment(1L);
    }

    @Test
    @DisplayName("Should complete appointment")
    @WithMockUser(roles = "DOCTOR")
    void shouldCompleteAppointment() throws Exception {
        testAppointment.setStatus(AppointmentStatus.COMPLETED);
        when(appointmentService.completeAppointment(1L)).thenReturn(testAppointment);

        mockMvc.perform(put("/api/appointments/1/complete")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(appointmentService, times(1)).completeAppointment(1L);
    }
}
