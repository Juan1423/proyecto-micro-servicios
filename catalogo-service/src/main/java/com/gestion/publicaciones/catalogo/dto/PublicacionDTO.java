package com.gestion.publicaciones.catalogo.dto;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.gestion.publicaciones.catalogo.domain.EstadoPublicacion;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Data
public class PublicacionDTO {
    private UUID id;
    private String titulo;
    private String resumen;
    private List<String> palabrasClave;
    private UUID autorPrincipalId;
    private String autor;
    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estado;
    private int versionActual;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private List<String> coAutoresIds;
    private String tipo;
    private String metadatos;
}

