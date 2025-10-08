package com.example.attendanceapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class EmailSender {

    // ✅ Fallback to hardcoded key if Railway env var is missing
    @Value("${BREVO_API_KEY:BVrD9yWz7PNFjb3M}")
    private String apiKey;

    private final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    // 🔐 For OTP delivery
    public void sendOtpEmail(String to, String otp) {
        String subject = "🔐 Your KL QUERIES OTP";
        String htmlBody = "<p>Hello 👋,</p>" +
                "<p>Your One-Time Password (OTP) is:</p>" +
                "<h2 style='color:#007BFF'>" + otp + "</h2>" +
                "<p>This OTP is valid for <strong>5 minutes</strong>.</p>" +
                "<p>Please do not share it with anyone.</p>" +
                "<br><p>Warm regards,<br><strong>KL QUERIES Team</strong></p>";
        sendEmail(to, subject, htmlBody);
    }

    // 📢 For query reply notification
    public void sendQueryReplyNotification(String to, String queryTitle) {
        String subject = "📢 Your KL Query Has Been Answered";
        String htmlBody = "<p>Hello 👋,</p>" +
                "<p>Your query titled <strong>\"" + queryTitle + "\"</strong> has received a response.</p>" +
                "<p>Visit <a href='https://klqueries.netlify.app'>KL QUERIES</a> to view the reply.</p>" +
                "<br><p>Best wishes,<br><strong>KL QUERIES Team</strong></p>";
        sendEmail(to, subject, htmlBody);
    }

    // 🧰 Core email method using Brevo API
    private void sendEmail(String to, String subject, String htmlBody) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> payload = new HashMap<>();
        payload.put("sender", Map.of("name", "KL QUERIES", "email", "98cc70001@smtp-brevo.com"));
        payload.put("to", List.of(Map.of("email", to)));
        payload.put("subject", subject);
        payload.put("htmlContent", htmlBody);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            System.out.println("🔑 Using Brevo API Key: " + apiKey); // Debug print
            ResponseEntity<String> response = restTemplate.postForEntity(BREVO_URL, request, String.class);
            System.out.println("📧 Brevo API response: " + response.getStatusCode());
            System.out.println("📨 Brevo response body: " + response.getBody());
        } catch (Exception e) {
            System.out.println("❌ Failed to send email via Brevo API: " + e.getMessage());
        }
    }
}
