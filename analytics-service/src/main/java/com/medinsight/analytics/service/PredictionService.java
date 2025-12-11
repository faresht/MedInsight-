package com.medinsight.analytics.service;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PredictionService {

    private final ChatClient chatClient;

    public PredictionService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String predictRelapseRisk(String patientHistory) {
        String message = """
                Analyze the following patient history and predict the risk of relapse.
                Provide a percentage score and a brief explanation.
                
                Patient History:
                {history}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("history", patientHistory));
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }

    public String predictBedOccupancy(String data) {
        String message = """
                Based on the current utilization data, predict the bed occupancy for the next 7 days.
                
                Data:
                {data}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("data", data));
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
    
    // Anomaly detection agent
    public String detectAnomalies(String logData) {
         String message = """
                You are a security anomaly detection agent. Analyze the following system logs for suspicious patterns.
                Report any potential security breaches or abnormal behaviors.
                
                Logs:
                {logs}
                """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("logs", logData));
        return chatClient.call(prompt).getResult().getOutput().getContent();
    }
}
