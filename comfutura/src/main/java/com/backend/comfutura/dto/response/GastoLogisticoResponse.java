package com.backend.comfutura.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastoLogisticoResponse {

    private Integer idGastoLog;
    private String concepto;
    private String unidadMedida;
    private String proveedor;
    private BigDecimal cantidad;

    //  alias frontend
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}
