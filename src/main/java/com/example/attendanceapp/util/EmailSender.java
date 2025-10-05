package com.example.attendanceapp.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailSender {

    @Autowired
    private JavaMailSender mailSender;

    // 🔐 For OTP delivery
    public void sendOtpEmail(String to, String otp) {
        String subject = "KL Queries OTP";
        String body = "Your OTP is: " + otp + "\nValid for 5 minutes.\nDo not share it with anyone.";
        sendEmail(to, subject, body);
    }

    // 📢 For query reply notification
    public void sendQueryReplyNotification(String to, String queryTitle) {
        String subject = "Your KL Query Has Been Answered";
        String body = "Your query titled \"" + queryTitle + "\" has received a response.\nVisit KL Queries to view it.";
        sendEmail(to, subject, body);
    }

    // 🧰 Core email method
    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(body);
            mailSender.send(msg);
            System.out.println("📧 Email sent to: " + to);
        } catch (Exception e) {
            System.out.println("❌ Failed to send email: " + e.getMessage());
        }
    }
}
