package com.backend.comfutura.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanillaTrabajoRequest {

    private Integer idOts;
    private Integer idTrabajador;
    private Integer idCargo;

    private LocalDate fecha;

    private BigDecimal costoDia;
    private BigDecimal cantDias;

    private Integer idBanco;
    private String moneda;
    private String cuenta;
    private String cci;
}
