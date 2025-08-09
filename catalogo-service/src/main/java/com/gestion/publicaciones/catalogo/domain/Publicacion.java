package com.gestion.publicaciones.catalogo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Publicacion {

    @Id
    private UUID id;
    private String titulo;
    private String resumen;
    private List<String> palabrasClave;
    @Enumerated(EnumType.STRING)
    private EstadoPublicacion estado;
    private int versionActual;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private UUID autorPrincipalId;
    private String autor; // New field for author's name
    private List<UUID> coAutoresIds;
    @Enumerated(EnumType.STRING)
    private TipoPublicacion tipo;
    @Column(columnDefinition = "jsonb")
    private String metadatos; // JSONB

}
