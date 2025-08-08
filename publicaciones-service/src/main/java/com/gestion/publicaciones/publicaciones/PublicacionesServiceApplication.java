package com.gestion.publicaciones.publicaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class PublicacionesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicacionesServiceApplication.class, args);
    }

}
