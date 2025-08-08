package com.gestion.publicaciones.publicaciones.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Capitulo {

    private int numero;
    private String titulo;
    private String resumenCapitulo;

}
