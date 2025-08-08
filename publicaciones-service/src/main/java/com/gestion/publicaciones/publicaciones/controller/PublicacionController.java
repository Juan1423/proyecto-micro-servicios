package com.gestion.publicaciones.publicaciones.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gestion.publicaciones.publicaciones.domain.Publicacion;
import com.gestion.publicaciones.publicaciones.service.PublicacionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionController {

    private final PublicacionService publicacionService;


    public PublicacionController(PublicacionService publicacionService) {
        this.publicacionService = publicacionService;
    }

    @GetMapping
    public ResponseEntity<List<Publicacion>> getAllPublicaciones() {
        List<Publicacion> publicaciones = publicacionService.getAllPublicaciones();
        return ResponseEntity.ok(publicaciones);
    }

    @PostMapping
    public Publicacion createPublicacion(@RequestBody Publicacion publicacion) throws JsonProcessingException {
        return publicacionService.createPublicacion(publicacion);
    }

    @PutMapping("/{id}/submit-for-review")
    public Publicacion submitForReview(@PathVariable UUID id) throws JsonProcessingException {
        return publicacionService.submitForReview(id);
    }

    @PutMapping("/{id}/return-for-changes")
    public Publicacion returnForChanges(@PathVariable UUID id) throws JsonProcessingException {
        return publicacionService.returnForChanges(id);
    }

    @PutMapping("/{id}/approve")
    public Publicacion approvePublication(@PathVariable UUID id) throws JsonProcessingException {
        return publicacionService.approvePublication(id);
    }

    @PutMapping("/{id}/publish")
    public Publicacion publishPublication(@PathVariable UUID id) throws JsonProcessingException {
        return publicacionService.publishPublication(id);
    }

    @PutMapping("/{id}/retire")
    public Publicacion retirePublication(@PathVariable UUID id) throws JsonProcessingException {
        return publicacionService.retirePublication(id);
    }
}