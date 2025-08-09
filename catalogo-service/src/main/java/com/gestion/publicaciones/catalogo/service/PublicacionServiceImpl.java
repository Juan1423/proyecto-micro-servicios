package com.gestion.publicaciones.catalogo.service;

import com.gestion.publicaciones.catalogo.domain.Publicacion;
import com.gestion.publicaciones.catalogo.dto.PublicacionDTO;
import com.gestion.publicaciones.catalogo.repository.PublicacionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PublicacionServiceImpl implements PublicacionService {

    private final PublicacionRepository publicacionRepository;

    public PublicacionServiceImpl(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

    @Override
    public Page<PublicacionDTO> listarPublicaciones(Pageable pageable) {
        Page<Publicacion> publicaciones = publicacionRepository.findAll(pageable);
        List<PublicacionDTO> dtos = publicaciones.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, publicaciones.getTotalElements());
    }

    @Override
    public PublicacionDTO obtenerPorId(String id) {
        Optional<Publicacion> publicacion = publicacionRepository.findById(UUID.fromString(id));
        return publicacion.map(this::convertToDto).orElse(null);
    }

    @Override
    public Page<PublicacionDTO> buscarPorTitulo(String titulo, Pageable pageable) {
        // Implement search by title if needed, requires a new method in repository
        return Page.empty(pageable);
    }

    @Override
    public Page<PublicacionDTO> buscarPorPalabrasClave(String palabrasClave, Pageable pageable) {
        // Implement search by keywords if needed, requires a new method in repository
        return Page.empty(pageable);
    }

    private PublicacionDTO convertToDto(Publicacion publicacion) {
        PublicacionDTO dto = new PublicacionDTO();
        dto.setId(publicacion.getId());
        dto.setTitulo(publicacion.getTitulo());
        dto.setResumen(publicacion.getResumen());
        dto.setPalabrasClave(publicacion.getPalabrasClave());
        dto.setEstado(publicacion.getEstado());
        dto.setVersionActual(publicacion.getVersionActual());
        dto.setFechaCreacion(publicacion.getFechaCreacion());
        dto.setFechaActualizacion(publicacion.getFechaActualizacion());
        dto.setAutorPrincipalId(publicacion.getAutorPrincipalId());
        dto.setAutor(publicacion.getAutor()); // Map the new autor field
        dto.setCoAutoresIds(publicacion.getCoAutoresIds() != null ? publicacion.getCoAutoresIds().stream().map(UUID::toString).collect(Collectors.toList()) : null);
        dto.setTipo(publicacion.getTipo().name());
        dto.setMetadatos(publicacion.getMetadatos());
        return dto;
    }
}
