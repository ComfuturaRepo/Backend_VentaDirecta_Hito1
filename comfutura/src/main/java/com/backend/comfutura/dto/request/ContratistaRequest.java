package com.backend.comfutura.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratistaRequest {

    private Integer idOts;
    private Integer idMaestroServicio;
    private Integer idProveedor;
    private BigDecimal cantidad;
    private BigDecimal costoUnitario;
}

