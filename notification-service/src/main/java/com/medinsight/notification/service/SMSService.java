package com.medinsight.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for sending SMS messages
 * This is a mock implementation - integrate with Twilio, AWS SNS, or similar
 * for production
 */
@Slf4j
@Service
public class SMSService {

    /**
     * Send SMS message (mock implementation)
     * 
     * For production, integrate with:
     * - Twilio: https://www.twilio.com/docs/sms
     * - AWS SNS: https://aws.amazon.com/sns/
     * - Vonage (Nexmo): https://www.vonage.com/communications-apis/sms/
     */
    public void sendSMS(String phoneNumber, String message) {
        // Mock implementation - just log the SMS
        log.info("=== SMS SENT (MOCK) ===");
        log.info("To: {}", phoneNumber);
        log.info("Message: {}", message);
        log.info("======================");

        // In production, you would call the SMS provider API here
        // Example with Twilio:
        // Message twilioMessage = Message.creator(
        // new PhoneNumber(phoneNumber),
        // new PhoneNumber(twilioPhoneNumber),
        // message
        // ).create();
    }

    /**
     * Validate phone number format
     */
    public boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return false;
        }
        // Basic validation - should start with + and contain only digits
        return phoneNumber.matches("^\\+?[1-9]\\d{1,14}$");
    }
}
