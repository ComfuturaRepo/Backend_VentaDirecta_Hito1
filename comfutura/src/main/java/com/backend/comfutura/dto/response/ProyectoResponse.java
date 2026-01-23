package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProyectoResponse {

    private Integer idProyecto;
    private String nombre;
    private Boolean activo;
}