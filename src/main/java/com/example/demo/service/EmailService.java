package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String toEmail, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset Request - Febi's Fashion");
        message.setText(
            "Hi,\n\n" +
            "We received a request to reset your password for your Febi's Fashion account.\n\n" +
            "Click the link below to reset your password:\n" +
            resetLink + "\n\n" +
            "This link will expire in 15 minutes.\n\n" +
            "If you didn't request this, you can safely ignore this email — your password will remain unchanged.\n\n" +
            "Thanks,\n" +
            "Febi's Fashion Team"
        );
        mailSender.send(message);
    }
}