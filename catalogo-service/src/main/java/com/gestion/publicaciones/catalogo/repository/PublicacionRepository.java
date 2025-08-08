package com.gestion.publicaciones.catalogo.repository;

import com.gestion.publicaciones.catalogo.entity.Publicacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PublicacionRepository extends JpaRepository<Publicacion, UUID> {

    @Query("SELECT p FROM Publicacion p " +
           "WHERE LOWER(p.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    Page<Publicacion> buscarPorTitulo(@Param("titulo") String titulo, Pageable pageable);

    @Query("SELECT p FROM Publicacion p " +
           "WHERE LOWER(p.palabrasClave) LIKE LOWER(CONCAT('%', :palabrasClave, '%'))")
    Page<Publicacion> buscarPorPalabrasClave(@Param("palabrasClave") String palabrasClave, Pageable pageable);
}
