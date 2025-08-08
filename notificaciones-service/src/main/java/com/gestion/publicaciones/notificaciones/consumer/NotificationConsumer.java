package com.gestion.publicaciones.notificaciones.consumer;

import com.gestion.publicaciones.notificaciones.service.NotificationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.gestion.publicaciones.notificaciones.config.RabbitMQConfig.QUEUE_NAME;

@Component
public class NotificationConsumer {

    private final NotificationService notificationService;

    @Autowired
    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveNotification(String message) {
        System.out.println("Received message: " + message);
        // In a real application, you would parse the message to extract recipient, subject, and eventType
        String recipient = "test@example.com"; // Placeholder
        String subject = "Notification from System"; // Placeholder
        String eventType = "unknown.event"; // Placeholder, derive from message content

        // Basic attempt to derive eventType from message if it's a simple string
        if (message != null && message.contains("publication")) {
            eventType = "publication.event";
        } else if (message != null && message.contains("user")) {
            eventType = "user.event";
        }

        notificationService.sendNotification(recipient, subject, message, eventType);
    }
}