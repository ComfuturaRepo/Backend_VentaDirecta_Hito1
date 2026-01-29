package com.backend.comfutura.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenOtResponse {

    private String tipoGasto;
    private BigDecimal monto;
}


