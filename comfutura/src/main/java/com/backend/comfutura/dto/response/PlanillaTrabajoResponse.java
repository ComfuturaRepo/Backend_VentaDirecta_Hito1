
package com.backend.comfutura.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanillaTrabajoResponse {

    private Integer idPlanilla;
    private String trabajador;
    private String cargo;

    private BigDecimal costoDia;
    private BigDecimal cantDias;

    private BigDecimal subtotal;
}
