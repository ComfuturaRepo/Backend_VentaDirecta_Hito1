package com.backend.comfutura.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrdenCompraRequestDTO {
    private Integer estadoOcId;
    private Integer otsId;
    private Integer maestroId;
    private Integer proveedorId;
    private BigDecimal cantidad;
    private BigDecimal costoUnitario;
    private String observacion;

}
