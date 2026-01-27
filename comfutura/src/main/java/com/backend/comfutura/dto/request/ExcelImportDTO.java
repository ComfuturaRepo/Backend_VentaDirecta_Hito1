package com.backend.comfutura.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ExcelImportDTO {
    private Integer filaExcel;
    private boolean valido = true;
    private String mensajeError;

    // Generado automáticamente al importar
    private Integer ot;

    // Campos de Excel (según tu especificación)
    private LocalDate fechaApertura;           // Obligatorio
    private String cliente;                    // Obligatorio
    private String descripcion;                    // Obligatorio
    private String area;                       // Obligatorio
    private String proyecto;                   // Obligatorio
    private String fase;                       // Obligatorio
    private String site;                       // Obligatorio
    private String region;                     // Obligatorio
    private String estado = "ASIGNACION";      // Obligatorio, siempre ASIGNACION

    private Integer otAnterior;                // Opcional

    // Responsables (todos obligatorios según tu comentario)
    private String JefaturaClienteSolicitante;    // Obligatorio
    private String AnalistaClienteSolicitante;    // Obligatorio
    private String CoordinadorTiCw;               // Obligatorio
    private String JefaturaResponsable;           // Obligatorio
    private String Liquidador;                    // Obligatorio
    private String Ejecutante;                    // Obligatorio
    private String AnalistaContable;              // Obligatorio

    // Getters y Setters generados automáticamente por @Data
}