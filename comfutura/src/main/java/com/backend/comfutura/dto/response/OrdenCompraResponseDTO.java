
package com.backend.comfutura.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraResponseDTO {

    private Integer idOc;

    // Estado (solo lo necesario)
    private Integer idEstadoOc;
    private String estadoNombre;

    private Integer idOts;
    private Integer idProveedor;
    private String formaPago;

    private BigDecimal subtotal;
    private BigDecimal igvPorcentaje;
    private BigDecimal igvTotal;
    private BigDecimal total;

    private LocalDateTime fechaOc;
    private String observacion;

    private List<OcDetalleResponseDTO> detalles;
}
