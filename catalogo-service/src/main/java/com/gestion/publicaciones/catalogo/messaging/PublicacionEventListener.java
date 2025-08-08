package com.gestion.publicaciones.catalogo.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestion.publicaciones.catalogo.entity.Publicacion;
import com.gestion.publicaciones.catalogo.repository.PublicacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

import static com.gestion.publicaciones.catalogo.config.RabbitMQConfig.QUEUE_NAME;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublicacionEventListener {

    private final PublicacionRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    @RabbitListener(queues = QUEUE_NAME)
    public void recibirEvento(String mensaje) {
        try {
            Map<String, Object> data = mapper.readValue(mensaje, Map.class);

            Publicacion pub = Publicacion.builder()
                    .titulo((String) data.get("titulo"))
                    .resumen((String) data.get("resumen"))
                    .palabrasClave((String) data.get("palabrasClave"))
                    .autor((String) data.get("autor"))
                    .tipo((String) data.get("tipo"))
                    .metadatos(mapper.writeValueAsString(data.get("metadatos")))
                    .fechaPublicacion(LocalDateTime.now())
                    .build();

            repository.save(pub);
            log.info("✅ Publicación guardada en catálogo: {}", pub.getTitulo());

        } catch (Exception e) {
            log.error("❌ Error procesando evento de publicación: {}", e.getMessage());
        }
    }
}
