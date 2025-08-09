package com.gestion.publicaciones.catalogo.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.publicaciones.catalogo.domain.Publicacion;
import com.gestion.publicaciones.catalogo.domain.EstadoPublicacion;
import com.gestion.publicaciones.catalogo.domain.TipoPublicacion;
import com.gestion.publicaciones.catalogo.repository.PublicacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.gestion.publicaciones.catalogo.config.RabbitMQConfig.QUEUE_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublicacionEventListener {

    private final PublicacionRepository repository;
    private final ObjectMapper mapper;

    @RabbitListener(queues = QUEUE_NAME)
    public void recibirEvento(String mensaje) {
        try {
            Map<String, Object> data = mapper.readValue(mensaje, Map.class);

            Publicacion pub;
            UUID publicacionId = UUID.fromString((String) data.get("id"));

            // Check if the publication already exists
            Optional<Publicacion> existingPubOpt = repository.findById(publicacionId);

            if (existingPubOpt.isPresent()) {
                // Update existing publication
                pub = existingPubOpt.get();
                pub.setTitulo((String) data.get("titulo"));
                pub.setResumen((String) data.get("resumen"));
                pub.setPalabrasClave((List<String>) data.get("palabrasClave"));
                pub.setAutorPrincipalId(UUID.fromString((String) data.get("autorPrincipalId")));
                pub.setTipo(TipoPublicacion.valueOf((String) data.get("tipo")));
                pub.setMetadatos(data.get("metadatos") != null ? mapper.writeValueAsString(data.get("metadatos")) : null);
                pub.setFechaActualizacion(LocalDateTime.now()); // Update timestamp
                pub.setEstado(EstadoPublicacion.valueOf((String) data.get("estado")));
                log.info("🔄 Actualizando publicación en catálogo: {}", pub.getTitulo());
            } else {
                // Create new publication
                pub = new Publicacion();
                pub.setId(publicacionId);
                pub.setTitulo((String) data.get("titulo"));
                pub.setResumen((String) data.get("resumen"));
                pub.setPalabrasClave((List<String>) data.get("palabrasClave"));
                pub.setAutorPrincipalId(UUID.fromString((String) data.get("autorPrincipalId")));
                pub.setTipo(TipoPublicacion.valueOf((String) data.get("tipo")));
                pub.setMetadatos(data.get("metadatos") != null ? mapper.writeValueAsString(data.get("metadatos")) : null);
                pub.setFechaActualizacion(LocalDateTime.now());
                pub.setEstado(EstadoPublicacion.valueOf((String) data.get("estado")));
                log.info("✅ Publicación guardada en catálogo: {}", pub.getTitulo());
            }

            repository.save(pub);

        } catch (Exception e) {
            log.error("❌ Error procesando evento de publicación:", e);
        }
    }
}
