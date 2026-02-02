package com.backend.comfutura.dto.request;

import lombok.Data;

@Data
 public class SiteDescripcionRequestDTO {
    private String descripcion;
    private Boolean activo = true;
}