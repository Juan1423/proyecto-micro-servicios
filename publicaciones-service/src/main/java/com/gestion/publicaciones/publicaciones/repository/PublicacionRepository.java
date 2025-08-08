package com.gestion.publicaciones.publicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.publicaciones.publicaciones.domain.Publicacion;

import java.util.UUID;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, UUID> {
}
