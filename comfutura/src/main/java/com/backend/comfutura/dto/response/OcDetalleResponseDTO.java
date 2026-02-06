package com.backend.comfutura.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcDetalleResponseDTO {

    private Integer idOcDetalle;

    // Maestro/Código
    private Integer idMaestro;
    private String codigo;
    private String descripcion;
    private String unidad;

    // Cantidades y precios
    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
    private Integer idOc; // ID de la orden de compra asociada


}
