package com.backend.comfutura.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder              // a
@AllArgsConstructor
@NoArgsConstructor
public class CompraDirectaDetalleResponse {

    // cabecera
    private Integer idCompraDirecta;
    private String nroRequerimiento;
    private String estado;

    // datos OT
    private String site;
    private String proyecto;
    private String cliente;

    // cálculos
    private Integer tiempoEjecucion;
    private BigDecimal importeTotal;

    // módulos
    private List<CronogramaResponse> cronograma;
    // (los demás vendrán luego)
}
