package com.medinsight.patient;

import com.medinsight.patient.repository.PatientRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @ActiveProfiles("test") // Optional, using DynamicPropertySource instead
class PatientServiceIntegrationTest {

        @LocalServerPort
        private Integer port;

        @Autowired
        private PatientRepository patientRepository;

        @DynamicPropertySource
        static void configureProperties(DynamicPropertyRegistry registry) {
                // H2 Configuration for reliable non-Docker testing
                registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL");
                registry.add("spring.datasource.username", () -> "sa");
                registry.add("spring.datasource.password", () -> "sa");
                registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.H2Dialect");
                registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        }

        @BeforeEach
        void setUp() {
                RestAssured.baseURI = "http://localhost:" + port;
                // patientRepository.deleteAll(); // Can cause constraint issues if not careful,
                // good to have but optional for simple test
        }

        @Test
        void shouldGetAllPatients_Unauthorized() {
                given()
                                .contentType(ContentType.JSON)
                                .when()
                                .get("/api/patients")
                                .then()
                                .statusCode(401);
        }
}
