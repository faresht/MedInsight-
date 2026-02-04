package com.medinsight.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medinsight.patient.dto.PatientDTO;
import com.medinsight.patient.entity.Patient;
import com.medinsight.patient.enums.BloodType;
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

    private PatientDTO testPatientDTO;
    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testPatientDTO = PatientDTO.builder()
                .id(1L)
                .userId("user-123")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .gender(Gender.MALE)
                .bloodType(BloodType.A_POSITIVE)
                .address("123 Main St, City, Country")
                .build();

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
                .address("123 Main St, City, Country")
                .deleted(false)
                .build();
    }

    @Test
    @DisplayName("Should create patient successfully")
    @WithMockUser(roles = "DOCTOR")
    void shouldCreatePatient() throws Exception {
        when(patientService.createPatient(any(PatientDTO.class))).thenReturn(testPatientDTO);

        mockMvc.perform(post("/api/patients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPatientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(patientService, times(1)).createPatient(any(PatientDTO.class));
    }

    @Test
    @DisplayName("Should get patient by ID successfully")
    @WithMockUser(roles = "DOCTOR")
    void shouldGetPatientById() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(Optional.of(testPatientDTO));

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
        PatientDTO patient2 = PatientDTO.builder()
                .id(2L)
                .userId("user-456")
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();

        List<PatientDTO> patients = Arrays.asList(testPatientDTO, patient2);
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
        PatientDTO updatedPatient = PatientDTO.builder()
                .id(1L)
                .userId("user-123")
                .firstName("John")
                .lastName("Doe Updated")
                .email("john.updated@example.com")
                .build();

        when(patientService.updatePatient(anyLong(), any(PatientDTO.class))).thenReturn(updatedPatient);

        mockMvc.perform(put("/api/patients/1")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Doe Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));

        verify(patientService, times(1)).updatePatient(anyLong(), any(PatientDTO.class));
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

    @Test
    @DisplayName("Should validate patient data on creation")
    @WithMockUser(roles = "DOCTOR")
    void shouldValidatePatientData() throws Exception {
        PatientDTO invalidPatient = PatientDTO.builder()
                .firstName("") // Invalid: empty
                .lastName("Doe")
                .email("invalid-email") // Invalid: not a valid email
                .build();

        mockMvc.perform(post("/api/patients")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPatient)))
                .andExpect(status().isBadRequest());
    }
}
