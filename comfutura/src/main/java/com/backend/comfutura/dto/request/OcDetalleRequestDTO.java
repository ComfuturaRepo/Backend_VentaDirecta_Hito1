package com.backend.comfutura.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcDetalleRequestDTO {

    private Integer idMaestro;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;

    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
}
