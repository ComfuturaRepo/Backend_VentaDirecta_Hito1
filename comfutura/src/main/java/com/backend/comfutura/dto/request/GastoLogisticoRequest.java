package com.backend.comfutura.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GastoLogisticoRequest {

    private Integer idOts;
    private String concepto;
    private Integer idUnidadMedida;
    private BigDecimal cantidad;
    private BigDecimal precio;
    private Integer idProveedor;
}

