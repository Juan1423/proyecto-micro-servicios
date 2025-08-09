package com.gestion.publicaciones.publicaciones.client;

import com.gestion.publicaciones.publicaciones.config.FeignClientConfig; // Import the config class
import com.gestion.publicaciones.publicaciones.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "auth-service", configuration = FeignClientConfig.class)
public interface AuthServiceClient {

    @GetMapping("/auth/users/{id}")
    UserResponseDTO getUser(@PathVariable("id") UUID id);
}
