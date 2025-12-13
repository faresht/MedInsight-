package com.medinsight.analytics.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    private final ChatClient chatClient;

    public PredictionService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String predictRelapseRisk(String patientHistory) {
        String message = """
                Analyze the following patient history and predict the risk of relapse.
                Provide a percentage score and a brief explanation.

                Patient History:
                {history}
                """;
        return chatClient.prompt()
                .user(u -> u.text(message).param("history", patientHistory))
                .call()
                .content();
    }

    public String predictBedOccupancy(String data) {
        String message = """
                Based on the current utilization data, predict the bed occupancy for the next 7 days.

                Data:
                {data}
                """;
        return chatClient.prompt()
                .user(u -> u.text(message).param("data", data))
                .call()
                .content();
    }

    // Anomaly detection agent
    public String detectAnomalies(String logData) {
        String message = """
                You are a security anomaly detection agent. Analyze the following system logs for suspicious patterns.
                Report any potential security breaches or abnormal behaviors.

                Logs:
                {logs}
                """;
        return chatClient.prompt()
                .user(u -> u.text(message).param("logs", logData))
                .call()
                .content();
    }
}
