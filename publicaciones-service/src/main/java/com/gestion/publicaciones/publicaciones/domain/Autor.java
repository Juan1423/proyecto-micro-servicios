package com.gestion.publicaciones.publicaciones.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nombres;
    private String apellidos;
    private String email;
    private String afiliacion;
    private String orcid;
    private String biografia;
    private String fotoUrl;
    private List<String> roles;

}
