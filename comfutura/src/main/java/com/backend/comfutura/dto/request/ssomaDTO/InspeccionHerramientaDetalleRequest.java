package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;

@Data
public class InspeccionHerramientaDetalleRequest {
    private Integer idHerramienta;
    private Boolean cumple;
    private String fotoUrl;
    private String observacion;
}