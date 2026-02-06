package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class MaestroServicioResponse {

    private Integer idMaestroServicio;
    private String codigo;
    private String descripcion;
    private Integer idUnidadMedida;
    private BigDecimal costoBase;
    private Boolean activo;
}