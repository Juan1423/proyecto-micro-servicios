package com.gestion.publicaciones.publicaciones.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Libro extends Publicacion {

    private String isbn;
    private int numeroPaginas;
    private String edicion;
    private List<Capitulo> capitulos;

}
