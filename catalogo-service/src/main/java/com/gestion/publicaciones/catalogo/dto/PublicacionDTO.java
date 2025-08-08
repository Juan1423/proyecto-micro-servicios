package com.gestion.publicaciones.catalogo.dto;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PublicacionDTO {
    private UUID id;
    private String titulo;
    private String resumen;
    private String palabrasClave;
    private String autor;
    private String tipo;
    private String metadatos;
    private LocalDateTime fechaPublicacion;
}
