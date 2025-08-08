package com.gestion.publicaciones.publicaciones.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public abstract class Publicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private List<UUID> coAutoresIds;
    @Enumerated(EnumType.STRING)
    private TipoPublicacion tipo;
    @Column(columnDefinition = "jsonb")
    private String metadatos; // JSONB

}
