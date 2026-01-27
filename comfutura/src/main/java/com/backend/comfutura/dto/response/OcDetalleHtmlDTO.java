package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class OcDetalleHtmlDTO {
    private String codigo;
    private BigDecimal cantidad;
    private String unidad;
    private String descripcion;
}
