package com.gestion.publicaciones.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "usuarios", schema = "auth_schema")
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
public class Usuario {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String nombres;
    
    @Column(nullable = false)
    private String apellidos;
    
    private String afiliacion;
    
    private String orcid;
    
    @Column(columnDefinition = "TEXT")
    private String biografia;
    
    private String fotoUrl;
    
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", schema = "auth_schema")
    private Set<Role> roles = new HashSet<>();
    
    @Column(nullable = false)
    private boolean activo = true;
    
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @LastModifiedDate
    private LocalDateTime fechaActualizacion;
    
    // Constructors
    public Usuario() {}
    
    public Usuario(String email, String password, String nombres, String apellidos) {
        this.email = email;
        this.password = password;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.roles.add(Role.ROLE_LECTOR); // Rol por defecto
    }
    
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}

enum Role {
    ROLE_AUTOR,
    ROLE_REVISOR, 
    ROLE_EDITOR,
    ROLE_ADMIN,
    ROLE_LECTOR
}