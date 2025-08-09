package com.gestion.publicaciones.publicaciones.dto;

import com.gestion.publicaciones.publicaciones.domain.Publicacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationEventDTO {
    private UUID id;
    private String titulo;
    private String resumen;
    private List<String> palabrasClave;
    private String estado;
    private int versionActual;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private UUID autorPrincipalId;
    private String autorPrincipalNombre; // Added field for author's name
    private List<UUID> coAutoresIds;
    private String tipo;
    private String metadatos;

    public PublicationEventDTO(Publicacion publicacion, String autorPrincipalNombre) {
        this.id = publicacion.getId();
        this.titulo = publicacion.getTitulo();
        this.resumen = publicacion.getResumen();
        this.palabrasClave = publicacion.getPalabrasClave();
        this.estado = publicacion.getEstado().name();
        this.versionActual = publicacion.getVersionActual();
        this.fechaCreacion = publicacion.getFechaCreacion();
        this.fechaActualizacion = publicacion.getFechaActualizacion();
        this.autorPrincipalId = publicacion.getAutorPrincipalId();
        this.autorPrincipalNombre = autorPrincipalNombre;
        this.coAutoresIds = publicacion.getCoAutoresIds();
        this.tipo = publicacion.getTipo().name();
        this.metadatos = publicacion.getMetadatos();
    }
}