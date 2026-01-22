package com.backend.comfutura.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OtDetalleRequest {

    @NotNull
    private Integer idMaestro;

    @NotNull
    private Integer idProveedor;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal cantidad;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal precioUnitario;
}
