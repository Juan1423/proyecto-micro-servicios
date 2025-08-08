package com.gestion.publicaciones.catalogo.service;



import com.gestion.publicaciones.catalogo.dto.PublicacionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PublicacionService {
    Page<PublicacionDTO> listarPublicaciones(Pageable pageable);
    PublicacionDTO obtenerPorId(String id);
    Page<PublicacionDTO> buscarPorTitulo(String titulo, Pageable pageable);
    Page<PublicacionDTO> buscarPorPalabrasClave(String palabrasClave, Pageable pageable);
}
