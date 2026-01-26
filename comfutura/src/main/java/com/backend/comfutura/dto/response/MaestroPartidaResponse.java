package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MaestroPartidaResponse {

    private Integer idMaestroPartida;
    private String codigo;
    private String descripcion;
    private Boolean activo;
}