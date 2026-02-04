package com.medinsight.analytics.dto.breast;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Request DTO for breast cancer diagnosis.
 * Contains multimodal data: imaging, genomic, and clinical.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreastCancerDiagnosisRequest {

    private String patientId;
    private ImagingData imagingData;
    private GenomicData genomicData;
    private ClinicalData clinicalData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ImagingData {
        private String imageData; // Base64 encoded or file path
        private String modality; // mammography, mri, ultrasound
        private Map<String, Object> metadata;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GenomicData {
        private Map<String, Double> geneExpressions;
        private java.util.List<String> mutations;
        private Map<String, Object> geneticMarkers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClinicalData {
        private Integer age;
        private Boolean familyHistory;
        private Integer firstDegreeRelativesWithBc;
        private Integer previousBiopsies;
        private Double bmi;
        private String menopauseStatus; // pre or post
        private Boolean hormoneTherapy;
        private Integer hormoneTherapyDuration;
        private Integer ageAtFirstBirth;
        private Map<String, Object> lifestyleFactors;
        private String breastDensity;
        private Boolean previousBreastCancer;
        private Boolean chestRadiationHistory;
    }
}
