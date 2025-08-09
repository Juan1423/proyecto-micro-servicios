package com.gestion.publicaciones.catalogo.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.publicaciones.catalogo.domain.Publicacion;
import com.gestion.publicaciones.catalogo.repository.PublicacionRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.gestion.publicaciones.catalogo.config.RabbitMQConfig.QUEUE_NAME;

@Component
public class PublicationConsumer {

    private final PublicacionRepository publicacionRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public PublicationConsumer(PublicacionRepository publicacionRepository, ObjectMapper objectMapper) {
        this.publicacionRepository = publicacionRepository;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = QUEUE_NAME)
    public void receivePublicationEvent(String message) {
        try {
            Map<String, Object> data = objectMapper.readValue(message, Map.class);
            Publicacion publicacion = objectMapper.convertValue(data, Publicacion.class);

            // Extract autorPrincipalNombre from the map and set it to the 'autor' field
            if (data.containsKey("autorPrincipalNombre")) {
                publicacion.setAutor((String) data.get("autorPrincipalNombre"));
            }

            System.out.println("Received publication: " + publicacion.getTitulo());
            publicacionRepository.save(publicacion);
            System.out.println("Publication saved to catalog: " + publicacion.getTitulo());
        } catch (Exception e) {
            System.err.println("Error processing publication event: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
