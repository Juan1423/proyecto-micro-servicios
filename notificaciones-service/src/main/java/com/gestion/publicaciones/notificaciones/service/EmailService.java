package com.gestion.publicaciones.notificaciones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            // You might want to set a 'from' address here, e.g., message.setFrom("noreply@yourdomain.com");

            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (MailException e) {
            System.err.println("Error sending email to " + to + ": " + e.getMessage());
            // Log the exception or handle it as needed
        }
    }
}