package com.backend.comfutura.dto.request.SSOMA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspeccionEPPDetalleDTO {
    private Integer idTrabajador;
    private Integer idEPP;
    private Boolean cumple;
    private String observacion;
}