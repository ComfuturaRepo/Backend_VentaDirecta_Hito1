// ExcelOtExportDTO.java
package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ExcelOtExportDTO {
    private Integer idOts;
    private Integer ot;
    private Integer idOtsAnterior;
    private String descripcion;
    private LocalDate fechaApertura;
    private Integer diasAsignados;
    private LocalDateTime fechaCreacion;
    private String activo;

    private String cliente;
    private String area;
    private String proyecto;
    private String fase;
    private String site;
    private String siteDescripcion;
    private String region;

    private String jefaturaClienteSolicitante;
    private String analistaClienteSolicitante;
    private String creador;
    private String coordinadorTiCw;
    private String jefaturaResponsable;
    private String liquidador;
    private String ejecutante;
    private String analistaContable;

    private String estadoOt;
}