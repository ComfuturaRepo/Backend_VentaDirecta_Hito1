package com.backend.comfutura.dto.response;




import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrdenCompraResponseDTO {
    private Integer idOc;
    private String estadoOcNombre;
    private String otsNombre;
    private String maestroCodigo;
    private String proveedorNombre;
    private BigDecimal cantidad;
    private BigDecimal costoUnitario;
    private LocalDateTime fechaOc;
    private String observacion;
}


