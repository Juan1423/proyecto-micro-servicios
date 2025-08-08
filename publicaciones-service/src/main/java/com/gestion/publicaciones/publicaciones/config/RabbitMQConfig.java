package com.gestion.publicaciones.publicaciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "publication.events";
    public static final String CATALOG_QUEUE_NAME = "catalog.publications";
    public static final String NOTIFICATIONS_QUEUE_NAME = "notifications.activity";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue catalogQueue() {
        return new Queue(CATALOG_QUEUE_NAME, true);
    }

    @Bean
    public Queue notificationsQueue() {
        return new Queue(NOTIFICATIONS_QUEUE_NAME, true);
    }

    @Bean
    public Binding catalogBinding(Queue catalogQueue, TopicExchange exchange) {
        return BindingBuilder.bind(catalogQueue).to(exchange).with("publication.published");
    }

    @Bean
    public Binding notificationsBinding(Queue notificationsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(notificationsQueue).to(exchange).with("publication.#");
    }

    @Bean
    public Binding userNotificationsBinding(Queue notificationsQueue, TopicExchange exchange) {
        return BindingBuilder.bind(notificationsQueue).to(exchange).with("user.#");
    }
}
