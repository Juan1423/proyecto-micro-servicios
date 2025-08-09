package com.gestion.publicaciones.notificaciones.consumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.publicaciones.notificaciones.service.NotificationService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.gestion.publicaciones.notificaciones.config.RabbitMQConfig.QUEUE_NAME;

@Component
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationConsumer(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void receiveNotification(Message message) {
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        String messageBody = new String(message.getBody());

        System.out.println("Received message with routing key: " + routingKey);

        if (routingKey.startsWith("user.")) {
            handleUserEvent(messageBody, routingKey);
        } else if (routingKey.startsWith("publication.")) {
            handlePublicationEvent(messageBody, routingKey);
        } else {
            System.err.println("Unknown routing key: " + routingKey);
        }
    }

    private void handleUserEvent(String email, String eventType) {
        String subject = "Registro Exitoso en el Sistema de Publicaciones";
        String body = "¡Bienvenido! Tu cuenta ha sido creada exitosamente.";
        notificationService.sendNotification(email, subject, body, eventType);
    }

    private void handlePublicationEvent(String jsonMessage, String eventType) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonMessage);
            String authorId = rootNode.get("autorPrincipalId").asText();
            String recipientEmail = notificationService.getUserEmail(authorId);

            if (recipientEmail != null) {
                String subject = "Novedades sobre tu publicación";
                String message = "Tu publicación ha sido actualizada. Estado: " + rootNode.get("estado").asText();
                notificationService.sendNotification(recipientEmail, subject, message, eventType);
            } else {
                System.err.println("Could not find email for author with ID: " + authorId);
            }
        } catch (Exception e) {
            System.err.println("Error processing publication event: " + e.getMessage());
        }
    }
}