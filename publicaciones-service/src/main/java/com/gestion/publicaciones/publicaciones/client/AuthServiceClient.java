package com.gestion.publicaciones.publicaciones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @GetMapping("/api/users/{id}")
    String getUser(@PathVariable("id") UUID id);
}
