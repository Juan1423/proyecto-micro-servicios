package com.gestion.publicaciones.publicaciones.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String aggregateId;
    private String aggregateType;
    private String eventType;
    @Column(columnDefinition = "TEXT") // Increase column length for JSON payload
    private String payloadJson;
    private String status;
    private LocalDateTime fechaCreacion;

}
