package com.medinsight.patient;

import com.medinsight.patient.dto.PatientDTO;
import com.medinsight.patient.enums.BloodType;
import com.medinsight.patient.enums.Gender;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DisplayName("Patient Service Integration Tests")
class PatientServiceIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    @DisplayName("Should create and retrieve patient")
    void shouldCreateAndRetrievePatient() {
        PatientDTO newPatient = PatientDTO.builder()
                .userId("test-user-001")
                .firstName("Integration")
                .lastName("Test")
                .email("integration.test@example.com")
                .phone("+1234567890")
                .dateOfBirth(LocalDate.of(1995, 5, 15))
                .gender(Gender.MALE)
                .bloodType(BloodType.O_POSITIVE)
                .address("123 Test Street")
                .build();

        // Create patient
        Integer patientId = given()
                .contentType(ContentType.JSON)
                .body(newPatient)
                .when()
                .post("/api/patients")
                .then()
                .statusCode(201)
                .body("firstName", equalTo("Integration"))
                .body("email", equalTo("integration.test@example.com"))
                .extract()
                .path("id");

        // Retrieve patient
        given()
                .when()
                .get("/api/patients/" + patientId)
                .then()
                .statusCode(200)
                .body("id", equalTo(patientId))
                .body("firstName", equalTo("Integration"))
                .body("lastName", equalTo("Test"));
    }

    @Test
    @DisplayName("Should update patient information")
    void shouldUpdatePatient() {
        // Create patient first
        PatientDTO newPatient = PatientDTO.builder()
                .userId("test-user-002")
                .firstName("Update")
                .lastName("Test")
                .email("update.test@example.com")
                .build();

        Integer patientId = given()
                .contentType(ContentType.JSON)
                .body(newPatient)
                .when()
                .post("/api/patients")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Update patient
        PatientDTO updatedPatient = PatientDTO.builder()
                .firstName("Updated")
                .lastName("Name")
                .email("updated.email@example.com")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(updatedPatient)
                .when()
                .put("/api/patients/" + patientId)
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Updated"))
                .body("lastName", equalTo("Name"));
    }

    @Test
    @DisplayName("Should soft delete patient")
    void shouldSoftDeletePatient() {
        // Create patient
        PatientDTO newPatient = PatientDTO.builder()
                .userId("test-user-003")
                .firstName("Delete")
                .lastName("Test")
                .email("delete.test@example.com")
                .build();

        Integer patientId = given()
                .contentType(ContentType.JSON)
                .body(newPatient)
                .when()
                .post("/api/patients")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        // Delete patient
        given()
                .when()
                .delete("/api/patients/" + patientId)
                .then()
                .statusCode(204);

        // Verify patient is not found (soft deleted)
        given()
                .when()
                .get("/api/patients/" + patientId)
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Should return 409 when email already exists")
    void shouldReturn409WhenEmailExists() {
        PatientDTO patient1 = PatientDTO.builder()
                .userId("test-user-004")
                .firstName("Duplicate")
                .lastName("Email")
                .email("duplicate@example.com")
                .build();

        // Create first patient
        given()
                .contentType(ContentType.JSON)
                .body(patient1)
                .when()
                .post("/api/patients")
                .then()
                .statusCode(201);

        // Try to create patient with same email
        PatientDTO patient2 = PatientDTO.builder()
                .userId("test-user-005")
                .firstName("Another")
                .lastName("Patient")
                .email("duplicate@example.com")
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(patient2)
                .when()
                .post("/api/patients")
                .then()
                .statusCode(409);
    }

    @Test
    @DisplayName("Should get all patients")
    void shouldGetAllPatients() {
        given()
                .when()
                .get("/api/patients")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThanOrEqualTo(0)));
    }
}
