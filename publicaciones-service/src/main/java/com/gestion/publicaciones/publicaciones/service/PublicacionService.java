package com.gestion.publicaciones.publicaciones.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.publicaciones.publicaciones.client.AuthServiceClient;
import com.gestion.publicaciones.publicaciones.domain.EstadoPublicacion;
import com.gestion.publicaciones.publicaciones.domain.OutboxEvent;
import com.gestion.publicaciones.publicaciones.domain.Publicacion;
import com.gestion.publicaciones.publicaciones.repository.OutboxEventRepository;
import com.gestion.publicaciones.publicaciones.repository.PublicacionRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PublicacionService {

    private final PublicacionRepository publicacionRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final AuthServiceClient authServiceClient;

    public PublicacionService(PublicacionRepository publicacionRepository, OutboxEventRepository outboxEventRepository, ObjectMapper objectMapper, AuthServiceClient authServiceClient) {
        this.publicacionRepository = publicacionRepository;
        this.outboxEventRepository = outboxEventRepository;
        this.objectMapper = objectMapper;
        this.authServiceClient = authServiceClient;
    }

    public Publicacion createPublicacion(Publicacion publicacion) throws JsonProcessingException {
        publicacion.setEstado(EstadoPublicacion.BORRADOR);
        publicacion.setFechaCreacion(LocalDateTime.now());
        publicacion.setFechaActualizacion(LocalDateTime.now());
        Publicacion savedPublicacion = publicacionRepository.save(publicacion);

        // Example of using Feign client to get author details
        String authorDetails = authServiceClient.getUser(savedPublicacion.getAutorPrincipalId());
        System.out.println("Fetched author details: " + authorDetails);

        saveOutboxEvent(savedPublicacion, "publication.submitted");

        return savedPublicacion;
    }

    public Publicacion submitForReview(UUID publicacionId) throws JsonProcessingException {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicacion not found"));
        publicacion.setEstado(EstadoPublicacion.EN_REVISION);
        publicacion.setFechaActualizacion(LocalDateTime.now());
        Publicacion updatedPublicacion = publicacionRepository.save(publicacion);

        saveOutboxEvent(updatedPublicacion, "publication.review.requested");

        return updatedPublicacion;
    }

    public Publicacion returnForChanges(UUID publicacionId) throws JsonProcessingException {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicacion not found"));
        publicacion.setEstado(EstadoPublicacion.CAMBIOS_SOLICITADOS);
        publicacion.setFechaActualizacion(LocalDateTime.now());
        Publicacion updatedPublicacion = publicacionRepository.save(publicacion);

        saveOutboxEvent(updatedPublicacion, "publication.review.returned");

        return updatedPublicacion;
    }

    public Publicacion approvePublication(UUID publicacionId) throws JsonProcessingException {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicacion not found"));
        publicacion.setEstado(EstadoPublicacion.APROBADO);
        publicacion.setFechaActualizacion(LocalDateTime.now());
        Publicacion updatedPublicacion = publicacionRepository.save(publicacion);

        saveOutboxEvent(updatedPublicacion, "publication.approved");

        return updatedPublicacion;
    }

    public Publicacion publishPublication(UUID publicacionId) throws JsonProcessingException {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicacion not found"));
        publicacion.setEstado(EstadoPublicacion.PUBLICADO);
        publicacion.setFechaActualizacion(LocalDateTime.now());
        Publicacion updatedPublicacion = publicacionRepository.save(publicacion);

        saveOutboxEvent(updatedPublicacion, "publication.published");

        return updatedPublicacion;
    }

    public Publicacion retirePublication(UUID publicacionId) throws JsonProcessingException {
        Publicacion publicacion = publicacionRepository.findById(publicacionId)
                .orElseThrow(() -> new RuntimeException("Publicacion not found"));
        publicacion.setEstado(EstadoPublicacion.RETIRADO);
        publicacion.setFechaActualizacion(LocalDateTime.now());
        Publicacion updatedPublicacion = publicacionRepository.save(publicacion);

        // No specific event mentioned for retirement, but we can add one if needed.
        // saveOutboxEvent(updatedPublicacion, "publication.retired");

        return updatedPublicacion;
    }

    public java.util.List<Publicacion> getAllPublicaciones() {
        return publicacionRepository.findAll();
    }

    private void saveOutboxEvent(Publicacion publicacion, String eventType) throws JsonProcessingException {
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setAggregateId(publicacion.getId().toString());
        outboxEvent.setAggregateType("publication");
        outboxEvent.setEventType(eventType);
        outboxEvent.setPayloadJson(objectMapper.writeValueAsString(publicacion));
        outboxEvent.setStatus("PENDIENTE");
        outboxEvent.setFechaCreacion(LocalDateTime.now());
        outboxEventRepository.save(outboxEvent);
    }
}
