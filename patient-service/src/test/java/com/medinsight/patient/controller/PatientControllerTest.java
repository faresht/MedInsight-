package com.medinsight.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medinsight.patient.entity.Patient;
import com.medinsight.patient.enums.Gender;
import com.medinsight.patient.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
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

@WebMvcTest(PatientController.class)
@DisplayName("Patient Controller Tests")
class PatientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
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
        @DisplayName("Should create patient successfully")
        @WithMockUser(roles = "DOCTOR")
        void shouldCreatePatient() throws Exception {
                when(patientService.createPatient(any(Patient.class))).thenReturn(testPatient);

                mockMvc.perform(post("/api/patients")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testPatient)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.firstName").value("John"))
                                .andExpect(jsonPath("$.lastName").value("Doe"))
                                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

                verify(patientService, times(1)).createPatient(any(Patient.class));
        }

        @Test
        @DisplayName("Should get patient by ID successfully")
        @WithMockUser(roles = "DOCTOR")
        void shouldGetPatientById() throws Exception {
                when(patientService.getPatientById(1L)).thenReturn(Optional.of(testPatient));

                mockMvc.perform(get("/api/patients/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1))
                                .andExpect(jsonPath("$.firstName").value("John"))
                                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

                verify(patientService, times(1)).getPatientById(1L);
        }

        @Test
        @DisplayName("Should return 404 when patient not found")
        @WithMockUser(roles = "DOCTOR")
        void shouldReturn404WhenPatientNotFound() throws Exception {
                when(patientService.getPatientById(anyLong())).thenReturn(Optional.empty());

                mockMvc.perform(get("/api/patients/999"))
                                .andExpect(status().isNotFound());

                verify(patientService, times(1)).getPatientById(999L);
        }

        @Test
        @DisplayName("Should get all patients successfully")
        @WithMockUser(roles = "DOCTOR")
        void shouldGetAllPatients() throws Exception {
                Patient patient2 = Patient.builder()
                                .id(2L)
                                .userId(UUID.randomUUID())
                                .firstName("Jane")
                                .lastName("Smith")
                                .email("jane.smith@example.com")
                                .medicalRecordNumber("MRN-67890")
                                .gender(Gender.FEMALE)
                                .dateOfBirth(LocalDate.of(1995, 5, 5))
                                .build();

                List<Patient> patients = Arrays.asList(testPatient, patient2);
                when(patientService.getAllPatients()).thenReturn(patients);

                mockMvc.perform(get("/api/patients"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].firstName").value("John"))
                                .andExpect(jsonPath("$[1].firstName").value("Jane"));

                verify(patientService, times(1)).getAllPatients();
        }

        @Test
        @DisplayName("Should update patient successfully")
        @WithMockUser(roles = "DOCTOR")
        void shouldUpdatePatient() throws Exception {
                Patient updatedPatient = Patient.builder()
                                .id(1L)
                                .userId(testPatient.getUserId())
                                .firstName("John")
                                .lastName("Doe Updated")
                                .email("john.updated@example.com")
                                .gender(Gender.MALE)
                                .dateOfBirth(testPatient.getDateOfBirth())
                                .medicalRecordNumber(testPatient.getMedicalRecordNumber())
                                .build();

                when(patientService.updatePatient(anyLong(), any(Patient.class))).thenReturn(updatedPatient);

                mockMvc.perform(put("/api/patients/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedPatient)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.lastName").value("Doe Updated"))
                                .andExpect(jsonPath("$.email").value("john.updated@example.com"));

                verify(patientService, times(1)).updatePatient(anyLong(), any(Patient.class));
        }

        @Test
        @DisplayName("Should delete patient successfully")
        @WithMockUser(roles = "ADMIN")
        void shouldDeletePatient() throws Exception {
                doNothing().when(patientService).deletePatient(1L);

                mockMvc.perform(delete("/api/patients/1")
                                .with(csrf()))
                                .andExpect(status().isNoContent());

                verify(patientService, times(1)).deletePatient(1L);
        }

        @Test
        @DisplayName("Should check if email exists")
        @WithMockUser(roles = "DOCTOR")
        void shouldCheckEmailExists() throws Exception {
                when(patientService.existsByEmail("john.doe@example.com")).thenReturn(true);

                mockMvc.perform(get("/api/patients/exists/email/john.doe@example.com"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("true"));

                verify(patientService, times(1)).existsByEmail("john.doe@example.com");
        }

        @Test
        @DisplayName("Should return 401 when unauthorized")
        void shouldReturn401WhenUnauthorized() throws Exception {
                mockMvc.perform(get("/api/patients"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return 403 when forbidden")
        @WithMockUser(roles = "PATIENT")
        void shouldReturn403WhenForbidden() throws Exception {
                mockMvc.perform(delete("/api/patients/1")
                                .with(csrf()))
                                .andExpect(status().isForbidden());
        }
}
