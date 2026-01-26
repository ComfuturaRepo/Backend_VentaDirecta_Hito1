
package com.backend.comfutura.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraRequestDTO {

    private Integer idEstadoOc;
    private Integer idOts;
    private Integer idProveedor;
    private String formaPago;

    private BigDecimal subtotal;
    private BigDecimal igvPorcentaje;
    private BigDecimal igvTotal;
    private BigDecimal total;

    private LocalDateTime fechaOc;
    private String observacion;

    private List<OcDetalleRequestDTO> detalles;
}
