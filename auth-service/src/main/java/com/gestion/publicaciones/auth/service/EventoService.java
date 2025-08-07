package com.gestion.publicaciones.auth.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventoService {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;

    public EventoService(RabbitTemplate rabbitTemplate,
                         @Value("${rabbitmq.exchange}") String exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void notificarRegistro(String email) {
        rabbitTemplate.convertAndSend(exchange, "user.registered", email);
    }

    public void notificarLogin(String email) {
        rabbitTemplate.convertAndSend(exchange, "user.login", email);
    }
}
