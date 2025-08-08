package com.gestion.publicaciones.notificaciones.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String recipient;
    private String subject;
    private String message;
    private LocalDateTime sentAt;
    private String status; // e.g., SENT, FAILED, PENDING
    private String eventType; // e.g., publication.approved, user.registered

}