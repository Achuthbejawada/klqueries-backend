package com.example.attendanceapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    // 🔐 For OTP delivery
    public void sendOtpEmail(String to, String otp) {
        String subject = "🔐 Your KL QUERIES OTP";
        String body = "<p>Hello there 👋,</p>" +
                "<p>Your One-Time Password (OTP) is:</p>" +
                "<h2 style='color:#007BFF'>" + otp + "</h2>" +
                "<p>This OTP is valid for <strong>5 minutes</strong>.</p>" +
                "<p>Please do not share it with anyone.</p>" +
                "<br><p>Warm regards,<br><strong>KL QUERIES Team</strong></p>";
        sendEmail(to, subject, body);
    }

    // 📢 For query reply notification
    public void sendQueryReplyNotification(String to, String queryTitle) {
        String subject = "📢 Your KL Query Has Been Answered";
        String body = "<p>Hello 👋,</p>" +
                "<p>Your query titled <strong>\"" + queryTitle + "\"</strong> has received a response.</p>" +
                "<p>Visit <a href='https://klqueries.netlify.app'>KL QUERIES</a> to view the reply.</p>" +
                "<br><p>Best wishes,<br><strong>KL QUERIES Team</strong></p>";
        sendEmail(to, subject, body);
    }

    // 🧰 Core email method
    private void sendEmail(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML
            helper.setFrom("98cc70001@smtp-brevo.com", "KL QUERIES");

            mailSender.send(message);
            System.out.println("📧 Email sent to: " + to);
        } catch (Exception e) {
            System.out.println("❌ Failed to send email: " + e.getMessage());
        }
    }
}
