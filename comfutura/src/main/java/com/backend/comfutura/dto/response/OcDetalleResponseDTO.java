package com.backend.comfutura.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OcDetalleResponseDTO {

    private Integer idOcDetalle;
    private Integer idOc;          // 👈 ESTE CAMPO FALTABA
    private Integer idMaestro;
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
}

