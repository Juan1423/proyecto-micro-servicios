package com.gestion.publicaciones.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .pathMatchers("/auth/**").permitAll()     // público (login, registro, etc.)
                .anyExchange().authenticated()            // lo demás requiere JWT
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt()) // activa verificación de JWT
            .build();
    }
}
