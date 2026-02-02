package com.backend.comfutura.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SiteDescripcionDTO {
    private Integer idSiteDescripcion;
    private String descripcion;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}