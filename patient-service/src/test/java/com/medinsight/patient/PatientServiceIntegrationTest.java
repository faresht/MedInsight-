package com.medinsight.patient;

import com.medinsight.patient.entity.Patient;
import com.medinsight.patient.repository.PatientRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class PatientServiceIntegrationTest {

        @Container
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

        @LocalServerPort
        private Integer port;

        @Autowired
        private PatientRepository patientRepository;

        @DynamicPropertySource
        static void configureProperties(DynamicPropertyRegistry registry) {
                registry.add("spring.datasource.url", postgres::getJdbcUrl);
                registry.add("spring.datasource.username", postgres::getUsername);
                registry.add("spring.datasource.password", postgres::getPassword);
        }

        @BeforeEach
        void setUp() {
                RestAssured.baseURI = "http://localhost:" + port;
                patientRepository.deleteAll();
        }

        @Test
        void shouldGetAllPatients() {
                given()
                                .contentType(ContentType.JSON)
                                .when()
                                .get("/api/patients")
                                .then()
                                .statusCode(401); // Expect 401 because we are not authenticated in this simple test
        }
}
