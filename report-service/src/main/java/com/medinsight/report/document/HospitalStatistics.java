package com.medinsight.report.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "hospital_statistics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HospitalStatistics {

    @Id
    private String id;

    private LocalDate reportDate;

    private Integer totalPatients;

    private Integer totalConsultations;

    private Double bedOccupancyRate;

    private Integer availableBeds;

    private Map<String, Integer> departmentStats;

    private LocalDateTime generatedAt;
}
