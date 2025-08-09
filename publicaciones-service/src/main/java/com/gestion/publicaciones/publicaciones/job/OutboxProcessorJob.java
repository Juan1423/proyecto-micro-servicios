package com.gestion.publicaciones.publicaciones.job;

import com.gestion.publicaciones.publicaciones.domain.OutboxEvent;
import com.gestion.publicaciones.publicaciones.repository.OutboxEventRepository;

import lombok.extern.slf4j.Slf4j; // Import Slf4j
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j // Add Slf4j annotation
public class OutboxProcessorJob {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    public OutboxProcessorJob(OutboxEventRepository outboxEventRepository, RabbitTemplate rabbitTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 10000)
    public void processOutboxEvents() {
        log.info("Starting OutboxProcessorJob. Processing events...");
        List<OutboxEvent> events = outboxEventRepository.findAll();
        log.info("Found {} outbox events to process.", events.size());

        for (OutboxEvent event : events) {
            try {
                rabbitTemplate.convertAndSend("publication.events", event.getEventType(), event.getPayloadJson());
                log.info("Sent event with ID {} and type {} to RabbitMQ.", event.getId(), event.getEventType());
                outboxEventRepository.delete(event);
                log.info("Deleted event with ID {} from outbox.", event.getId());
            } catch (Exception e) {
                log.error("Error processing outbox event with ID {}:", event.getId(), e);
                // Optionally, update event status to FAILED or similar
            }
        }
        log.info("Finished OutboxProcessorJob.");
    }
}
