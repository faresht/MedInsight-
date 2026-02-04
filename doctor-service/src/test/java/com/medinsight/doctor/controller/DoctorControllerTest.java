package com.medinsight.doctor.controller;

import com.medinsight.doctor.config.TestSecurityConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medinsight.doctor.entity.Doctor;
import com.medinsight.doctor.service.DoctorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
@org.springframework.context.annotation.Import(TestSecurityConfig.class)
@DisplayName("Doctor Controller Tests")
class DoctorControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
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
        @WithMockUser(roles = "ADMIN")
        void shouldCreateDoctor() throws Exception {
                when(doctorService.createDoctor(any(Doctor.class))).thenReturn(testDoctor);

                mockMvc.perform(post("/api/doctors")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testDoctor)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.firstName").value("Gregory"))
                                .andExpect(jsonPath("$.lastName").value("House"));

                verify(doctorService, times(1)).createDoctor(any(Doctor.class));
        }

        @Test
        @DisplayName("Should get doctor by ID successfully")
        @WithMockUser(roles = "PATIENT")
        void shouldGetDoctorById() throws Exception {
                when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(testDoctor));

                mockMvc.perform(get("/api/doctors/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.email").value("g.house@medinsight.com"));

                verify(doctorService, times(1)).getDoctorById(1L);
        }

        @Test
        @DisplayName("Should return 404 when doctor not found by ID")
        @WithMockUser(roles = "PATIENT")
        void shouldReturn404WhenDoctorNotFoundById() throws Exception {
                when(doctorService.getDoctorById(anyLong())).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/doctors/999"))
                                .andExpect(status().isNotFound());

                verify(doctorService, times(1)).getDoctorById(999L);
        }

        @Test
        @DisplayName("Should get doctor by User ID successfully")
        @WithMockUser(roles = "PATIENT")
        void shouldGetDoctorByUserId() throws Exception {
                when(doctorService.getDoctorByUserId(userId)).thenReturn(Optional.of(testDoctor));

                mockMvc.perform(get("/api/doctors/user/" + userId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.userId").value(userId.toString()))
                                .andExpect(jsonPath("$.firstName").value("Gregory"));

                verify(doctorService, times(1)).getDoctorByUserId(userId);
        }

        @Test
        @DisplayName("Should get all active doctors successfully")
        @WithMockUser(roles = "PATIENT")
        void shouldGetAllActiveDoctors() throws Exception {
                Doctor doctor2 = Doctor.builder()
                                .id(2L)
                                .firstName("James")
                                .lastName("Wilson")
                                .specialization("ONCOLOGY")
                                .active(true)
                                .build();

                List<Doctor> doctors = Arrays.asList(testDoctor, doctor2);
                when(doctorService.getAllActiveDoctors()).thenReturn(doctors);

                mockMvc.perform(get("/api/doctors"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].firstName").value("Gregory"))
                                .andExpect(jsonPath("$[1].firstName").value("James"));

                verify(doctorService, times(1)).getAllActiveDoctors();
        }

        @Test
        @DisplayName("Should get doctors by specialization successfully")
        @WithMockUser(roles = "PATIENT")
        void shouldGetDoctorsBySpecialization() throws Exception {
                List<Doctor> doctors = Arrays.asList(testDoctor);
                when(doctorService.getDoctorsBySpecialization("DIAGNOSTIC_MEDICINE")).thenReturn(doctors);

                mockMvc.perform(get("/api/doctors/specialization/DIAGNOSTIC_MEDICINE"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1))
                                .andExpect(jsonPath("$[0].specialization").value("DIAGNOSTIC_MEDICINE"));

                verify(doctorService, times(1)).getDoctorsBySpecialization("DIAGNOSTIC_MEDICINE");
        }

        @Test
        @DisplayName("Should update doctor successfully")
        @WithMockUser(roles = "DOCTOR")
        void shouldUpdateDoctor() throws Exception {
                Doctor updatedDoctor = Doctor.builder()
                                .id(1L)
                                .firstName("Gregory Updated")
                                .lastName("House")
                                .specialization("DIAGNOSTIC_MEDICINE")
                                .build();

                when(doctorService.updateDoctor(anyLong(), any(Doctor.class))).thenReturn(updatedDoctor);

                mockMvc.perform(put("/api/doctors/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedDoctor)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstName").value("Gregory Updated"));

                verify(doctorService, times(1)).updateDoctor(anyLong(), any(Doctor.class));
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent doctor")
        @WithMockUser(roles = "DOCTOR")
        void shouldReturn404WhenUpdatingNonExistentDoctor() throws Exception {
                when(doctorService.updateDoctor(anyLong(), any(Doctor.class)))
                                .thenThrow(new RuntimeException("Doctor not found"));

                mockMvc.perform(put("/api/doctors/999")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testDoctor)))
                                .andExpect(status().isNotFound());

                verify(doctorService, times(1)).updateDoctor(anyLong(), any(Doctor.class));
        }

        @Test
        @DisplayName("Should deactivate doctor successfully")
        @WithMockUser(roles = "ADMIN")
        void shouldDeactivateDoctor() throws Exception {
                doNothing().when(doctorService).deactivateDoctor(1L);

                mockMvc.perform(put("/api/doctors/1/deactivate")
                                .with(csrf()))
                                .andExpect(status().isNoContent());

                verify(doctorService, times(1)).deactivateDoctor(1L);
        }
}
