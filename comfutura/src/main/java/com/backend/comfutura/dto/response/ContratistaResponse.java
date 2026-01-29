
package com.backend.comfutura.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratistaResponse {

    private Integer idContratistaOt;
    private String servicio;
    private String proveedor;
    private String unidadMedida;
    private BigDecimal cantidad;

    //  alias para el frontend
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
}

