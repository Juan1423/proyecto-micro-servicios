package com.gestion.publicaciones.catalogo.controller;

import com.gestion.publicaciones.catalogo.dto.PublicacionDTO;
import com.gestion.publicaciones.catalogo.service.PublicacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/catalogo")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionService service;

    @GetMapping
    public Page<PublicacionDTO> listar(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.listarPublicaciones(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public PublicacionDTO obtenerPorId(@PathVariable String id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/buscar/titulo")
    public Page<PublicacionDTO> buscarPorTitulo(@RequestParam String titulo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.buscarPorTitulo(titulo, PageRequest.of(page, size));
    }

    @GetMapping("/buscar/palabras")
    public Page<PublicacionDTO> buscarPorPalabrasClave(@RequestParam String palabras,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return service.buscarPorPalabrasClave(palabras, PageRequest.of(page, size));
    }

}
