
package com.backend.comfutura.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompraDirectaResponse {

    private Integer idCompraDirecta;
    private String nroRequerimiento;
    private Integer idOts;

    private String site;
    private String proyecto;
    private String cliente;

    private Integer tiempoEjecucion;
    private BigDecimal importeTotal;

    private String estado;
}


