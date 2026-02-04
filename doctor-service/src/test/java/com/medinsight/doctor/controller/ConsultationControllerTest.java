package com.medinsight.doctor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medinsight.doctor.entity.Consultation;
import com.medinsight.doctor.service.ConsultationService;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConsultationController.class)
@DisplayName("Consultation Controller Tests")
class ConsultationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
                .visibleInPortal(true)
                .build();
    }

    @Test
    @DisplayName("Should create consultation successfully")
    @WithMockUser(roles = "DOCTOR")
    void shouldCreateConsultation() throws Exception {
        when(consultationService.createConsultation(any(Consultation.class))).thenReturn(testConsultation);

        mockMvc.perform(post("/api/consultations")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testConsultation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.diagnosis").value("Migraine"));

        verify(consultationService, times(1)).createConsultation(any(Consultation.class));
    }

    @Test
    @DisplayName("Should get consultation by ID successfully")
    @WithMockUser(roles = "DOCTOR")
    void shouldGetConsultationById() throws Exception {
        when(consultationService.getConsultationById(1L)).thenReturn(testConsultation);

        mockMvc.perform(get("/api/consultations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(consultationService, times(1)).getConsultationById(1L);
    }

    @Test
    @DisplayName("Should get consultation by appointment ID")
    @WithMockUser(roles = "DOCTOR")
    void shouldGetConsultationByAppointmentId() throws Exception {
        when(consultationService.getConsultationByAppointmentId(500L)).thenReturn(testConsultation);

        mockMvc.perform(get("/api/consultations/appointment/500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appointmentId").value(500));

        verify(consultationService, times(1)).getConsultationByAppointmentId(500L);
    }

    @Test
    @DisplayName("Should get consultations by patient ID")
    @WithMockUser(roles = "DOCTOR")
    void shouldGetConsultationsByPatientId() throws Exception {
        when(consultationService.getConsultationsByPatientId(100L))
                .thenReturn(Collections.singletonList(testConsultation));

        mockMvc.perform(get("/api/consultations/patient/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(consultationService, times(1)).getConsultationsByPatientId(100L);
    }

    @Test
    @DisplayName("Should get consultations by doctor ID")
    @WithMockUser(roles = "DOCTOR")
    void shouldGetConsultationsByDoctorId() throws Exception {
        when(consultationService.getConsultationsByDoctorId(200L))
                .thenReturn(Collections.singletonList(testConsultation));

        mockMvc.perform(get("/api/consultations/doctor/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(consultationService, times(1)).getConsultationsByDoctorId(200L);
    }

    @Test
    @DisplayName("Should get patient portal consultations")
    @WithMockUser(roles = "PATIENT")
    void shouldGetPatientPortalConsultations() throws Exception {
        when(consultationService.getPatientPortalConsultations(100L))
                .thenReturn(Collections.singletonList(testConsultation));

        mockMvc.perform(get("/api/consultations/patient/100/portal"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].visibleInPortal").value(true));

        verify(consultationService, times(1)).getPatientPortalConsultations(100L);
    }

    @Test
    @DisplayName("Should update consultation successfully")
    @WithMockUser(roles = "DOCTOR")
    void shouldUpdateConsultation() throws Exception {
        when(consultationService.updateConsultation(anyLong(), any(Consultation.class))).thenReturn(testConsultation);

        mockMvc.perform(put("/api/consultations/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testConsultation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("Migraine"));

        verify(consultationService, times(1)).updateConsultation(anyLong(), any(Consultation.class));
    }
}
