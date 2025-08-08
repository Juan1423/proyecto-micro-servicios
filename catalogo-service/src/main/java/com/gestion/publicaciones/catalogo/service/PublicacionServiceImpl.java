package com.gestion.publicaciones.catalogo.service;

import com.gestion.publicaciones.catalogo.dto.PublicacionDTO;
import com.gestion.publicaciones.catalogo.entity.Publicacion;
import com.gestion.publicaciones.catalogo.repository.PublicacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository repository;

    @Override
    public Page<PublicacionDTO> listarPublicaciones(Pageable pageable) {
        return repository.findAll(pageable).map(this::convertToDTO);
    }

    @Override
    public PublicacionDTO obtenerPorId(String id) {
        Publicacion publicacion = repository.findById(UUID.fromString(id))
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));
        return convertToDTO(publicacion);
    }

    private PublicacionDTO convertToDTO(Publicacion entity) {
        PublicacionDTO dto = new PublicacionDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public Page<PublicacionDTO> buscarPorTitulo(String titulo, Pageable pageable) {
        return repository.buscarPorTitulo(titulo, pageable).map(this::convertToDTO);
    }

    @Override
    public Page<PublicacionDTO> buscarPorPalabrasClave(String palabrasClave, Pageable pageable) {
        return repository.buscarPorPalabrasClave(palabrasClave, pageable).map(this::convertToDTO);
    }

}
