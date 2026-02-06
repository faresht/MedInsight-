package com.medinsight.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private String recipientUserId;
    private String recipientEmail;
    private String subject;
    private String message;
    private String type; // EMAIL, SMS
    private String channel; // EMAIL
}
