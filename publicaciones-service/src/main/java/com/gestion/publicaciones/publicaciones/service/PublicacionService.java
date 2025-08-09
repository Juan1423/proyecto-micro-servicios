package com.gestion.publicaciones.publicaciones.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.publicaciones.publicaciones.client.AuthServiceClient;
import com.gestion.publicaciones.publicaciones.domain.EstadoPublicacion;
import com.gestion.publicaciones.publicaciones.domain.OutboxEvent;
import com.gestion.publicaciones.publicaciones.domain.Publicacion;
import com.gestion.publicaciones.publicaciones.dto.PublicationEventDTO;
import com.gestion.publicaciones.publicaciones.dto.UserResponseDTO;
import com.gestion.publicaciones.publicaciones.repository.OutboxEventRepository;
import com.gestion.publicaciones.publicaciones.repository.PublicacionRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        UserResponseDTO authorDetails = authServiceClient.getUser(savedPublicacion.getAutorPrincipalId());
        System.out.println("Fetched author details: " + authorDetails.getNombre());

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

        // Fetch author details to include in the event payload
        UserResponseDTO authorDetails = authServiceClient.getUser(updatedPublicacion.getAutorPrincipalId());
        String autorPrincipalNombre = authorDetails.getNombre();

        // Create a DTO for the event payload
        PublicationEventDTO eventPayload = new PublicationEventDTO(updatedPublicacion, autorPrincipalNombre);

        saveOutboxEvent(eventPayload, "publication.published");

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

    public Publicacion updatePublicacion(UUID id, Publicacion updatedPublicacion) throws JsonProcessingException {
        Publicacion existingPublicacion = publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicacion not found"));

        // Update fields from updatedPublicacion to existingPublicacion
        existingPublicacion.setTitulo(updatedPublicacion.getTitulo());
        existingPublicacion.setTipo(updatedPublicacion.getTipo());
        existingPublicacion.setAutorPrincipalId(updatedPublicacion.getAutorPrincipalId());
        existingPublicacion.setResumen(updatedPublicacion.getResumen());
        existingPublicacion.setPalabrasClave(updatedPublicacion.getPalabrasClave());
        // Add other fields as needed

        existingPublicacion.setFechaActualizacion(LocalDateTime.now());
        Publicacion savedPublicacion = publicacionRepository.save(existingPublicacion);

        saveOutboxEvent(savedPublicacion, "publication.updated");

        return savedPublicacion;
    }

    public java.util.List<Publicacion> getAllPublicaciones() {
        return publicacionRepository.findAll();
    }

    public Publicacion getPublicacionById(UUID id) {
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Publicacion not found with ID: " + id));
    }

    public List<Publicacion> getPublicacionesByAutor(UUID autorId) {
        return publicacionRepository.findByAutorPrincipalId(autorId);
    }

    public void deletePublicacion(UUID id) {
        if (!publicacionRepository.existsById(id)) {
            throw new RuntimeException("Publicacion not found");
        }
        publicacionRepository.deleteById(id);
        // Optionally, save an outbox event for deletion
        // saveOutboxEvent(id.toString(), "publication.deleted"); // You might need a different payload for deletion events
    }

    private void saveOutboxEvent(Object payload, String eventType) throws JsonProcessingException {
        OutboxEvent outboxEvent = new OutboxEvent();
        // Assuming payload has an 'id' field, or you pass it separately
        // For PublicationEventDTO, it has an ID
        if (payload instanceof PublicationEventDTO) {
            outboxEvent.setAggregateId(((PublicationEventDTO) payload).getId().toString());
        } else if (payload instanceof Publicacion) {
            outboxEvent.setAggregateId(((Publicacion) payload).getId().toString());
        } else {
            // Handle other types or throw an error
            outboxEvent.setAggregateId("unknown");
        }
        outboxEvent.setAggregateType("publication");
        outboxEvent.setEventType(eventType);
        outboxEvent.setPayloadJson(objectMapper.writeValueAsString(payload));
        outboxEvent.setStatus("PENDIENTE");
        outboxEvent.setFechaCreacion(LocalDateTime.now());
        outboxEventRepository.save(outboxEvent);
    }
}
