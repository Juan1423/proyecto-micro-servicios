package com.gestion.publicaciones.notificaciones.service;

import com.gestion.publicaciones.notificaciones.domain.NotificationLog;
import com.gestion.publicaciones.notificaciones.repository.NotificationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationLogRepository notificationLogRepository;
    private final EmailService emailService;

    @Autowired
    public NotificationService(NotificationLogRepository notificationLogRepository, EmailService emailService) {
        this.notificationLogRepository = notificationLogRepository;
        this.emailService = emailService;
    }

    public void sendNotification(String recipient, String subject, String message, String eventType) {
        // Here you would implement the logic to send notifications
        // This could involve sending emails, WebSocket messages, etc.
        System.out.println("Sending notification to " + recipient + " - Subject: " + subject + ", Message: " + message);

        // Example: Send email
        emailService.sendSimpleEmail(recipient, subject, message);

        // Log the notification
        NotificationLog log = new NotificationLog();
        log.setRecipient(recipient);
        log.setSubject(subject);
        log.setMessage(message);
        log.setSentAt(LocalDateTime.now());
        log.setStatus("SENT"); // Or PENDING, FAILED based on actual sending result
        log.setEventType(eventType);
        notificationLogRepository.save(log);
    }
}