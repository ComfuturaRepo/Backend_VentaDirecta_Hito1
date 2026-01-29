package com.backend.comfutura.dto.request;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MaterialRequest {

    private Integer idOts;
    private Integer idMaestroMaterial;
    private BigDecimal cantidad;
    private BigDecimal costoUnitario; //  IMPORTANTE
}



