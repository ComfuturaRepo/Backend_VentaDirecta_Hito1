// src/main/java/com/backend/comfutura/dto/response/OtListDto.java
package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OtListDto {
    private Integer idOts;           // ← NUEVO: para toggle y edición
    private Integer ot;
    private LocalDate fechaApertura;
    private String estadoOt;
    private String regionNombre;
    private String siteNombre;
    private String faseNombre;
    private String descripcion;
    private Boolean activo;          // ← NUEVO: para el botón toggle y badge
}