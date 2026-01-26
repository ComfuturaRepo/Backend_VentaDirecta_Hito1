// src/main/java/com/backend/comfutura/dto/response/OtListDto.java
package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OtListDto {
    private Integer idOts;
    private Integer ot;
    private LocalDate fechaApertura;
    private String estadoOt;
    private String cliente_id;
    private String siteNombre;
    private String site_descripcion;
    private String cliente;
    private String proyecto;
    private String region;
    private Boolean activo;
}