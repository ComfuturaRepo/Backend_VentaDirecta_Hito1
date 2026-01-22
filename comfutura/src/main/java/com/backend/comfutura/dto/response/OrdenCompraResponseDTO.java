package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrdenCompraResponseDTO {

    private Integer idOc;

    private Integer estadoOcId;
    private String  estadoOcNombre;

    private Integer otsId;
    private String  otsNombre;

    private Integer maestroId;
    private String  maestroCodigo;

    private Integer proveedorId;
    private String  proveedorNombre;

    private BigDecimal cantidad;
    private BigDecimal costoUnitario;
    private LocalDateTime fechaOc;
    private String observacion;

}