package com.gestion.publicaciones.catalogo.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "publicaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Publicacion {

    @Id
    @GeneratedValue
    private UUID id;

    private String titulo;
    private String resumen;
    private String palabrasClave;
    private String autor;
    private String tipo; // ARTICULO o LIBRO
    private String metadatos; // JSON en String
    private LocalDateTime fechaPublicacion;
}
