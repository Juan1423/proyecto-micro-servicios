package com.gestion.publicaciones.publicaciones.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Articulo extends Publicacion {

    private String revistaObjetivo;
    private String seccion;
    private List<String> referenciasBibliograficas;
    private int figuras;
    private String tablaFigurasMetadata;

}
