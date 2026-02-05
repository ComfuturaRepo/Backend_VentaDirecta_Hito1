package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;

@Data
public class InspeccionEppDetalleRequest {
    private Integer idTrabajador;
    private Integer idEpp;
    private Boolean cumple;
    private String observacion;
}