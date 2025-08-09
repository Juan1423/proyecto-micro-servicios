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
        // The 'message' here is the email address sent by auth-service
        String recipient = message; 
        String subject = "Registro Exitoso en el Sistema de Publicaciones"; 
        String eventType = "user.registered"; // Assuming this consumer primarily handles user registration for now

        // In a more complex scenario, you'd parse a JSON message to get eventType and other details
        // For now, we'll assume the message is the recipient email and the event is user.registered

        notificationService.sendNotification(recipient, subject, "¡Bienvenido! Tu cuenta ha sido creada exitosamente.", eventType);
    }
}