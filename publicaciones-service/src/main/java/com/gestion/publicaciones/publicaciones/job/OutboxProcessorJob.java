package com.gestion.publicaciones.publicaciones.job;

import com.gestion.publicaciones.publicaciones.domain.OutboxEvent;
import com.gestion.publicaciones.publicaciones.repository.OutboxEventRepository;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxProcessorJob {

    private final OutboxEventRepository outboxEventRepository;
    private final RabbitTemplate rabbitTemplate;

    public OutboxProcessorJob(OutboxEventRepository outboxEventRepository, RabbitTemplate rabbitTemplate) {
        this.outboxEventRepository = outboxEventRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 10000)
    public void processOutboxEvents() {
        List<OutboxEvent> events = outboxEventRepository.findAll();
        for (OutboxEvent event : events) {
            rabbitTemplate.convertAndSend(event.getAggregateType(), event.getEventType(), event.getPayloadJson());
            outboxEventRepository.delete(event);
        }
    }
}
