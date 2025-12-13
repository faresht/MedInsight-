package com.medinsight.analytics.document;

import com.medinsight.analytics.enums.ModelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "ai_models")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIModel {

    @Id
    private String id;

    private String name;

    private String version;

    private ModelType modelType;

    private String framework;

    private LocalDateTime trainedAt;

    private Map<String, Double> performanceMetrics;

    private boolean active;
}
