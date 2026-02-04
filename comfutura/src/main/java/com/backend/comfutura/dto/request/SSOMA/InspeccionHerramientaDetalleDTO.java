package com.backend.comfutura.dto.request.SSOMA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspeccionHerramientaDetalleDTO {
    private Integer idHerramienta;
    private Boolean cumple;
    private String fotoUrl;
    private String observacion;
}