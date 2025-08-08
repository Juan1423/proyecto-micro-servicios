package com.gestion.publicaciones.notificaciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "publication.events";
    public static final String QUEUE_NAME = "notifications.activity";
    public static final String PUBLICATION_ROUTING_KEY = "publication.#"; // Matches publication.* and sub-topics
    public static final String USER_ROUTING_KEY = "user.#"; // Matches user.* and sub-topics

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Binding publicationBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(PUBLICATION_ROUTING_KEY);
    }

    @Bean
    public Binding userBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(USER_ROUTING_KEY);
    }
}