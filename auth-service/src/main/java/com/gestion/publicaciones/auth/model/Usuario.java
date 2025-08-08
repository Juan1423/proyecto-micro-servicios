package com.gestion.publicaciones.auth.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    private UUID id;

    private String nombre;

    @Column(unique = true)
    private String email;

    private String password; // Se guarda hasheada

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;
}

