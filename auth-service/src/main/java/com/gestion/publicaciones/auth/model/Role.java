package com.gestion.publicaciones.auth.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // Predefined role names as constants for easy access
    public static final String ROLE_AUTOR = "ROLE_AUTOR";
    public static final String ROLE_REVISOR = "ROLE_REVISOR";
    public static final String ROLE_EDITOR = "ROLE_EDITOR";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_LECTOR = "ROLE_LECTOR";

    public Role(String name) {
        this.name = name;
    }
}