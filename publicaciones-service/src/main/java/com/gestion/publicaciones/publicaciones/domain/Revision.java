package com.gestion.publicaciones.publicaciones.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Revision {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID publicacionId;
    private UUID revisorId;
    private EstadoRevision estadoRevision;
    private String comentarios;
    private String historialCambios;

}
