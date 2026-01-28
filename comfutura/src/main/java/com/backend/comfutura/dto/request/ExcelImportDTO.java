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

    // Temporal para guardar el request validado (necesario según el servicio)
    private OtCreateRequest tempRequest;

    // Campos de Excel (según tu plantilla)
    private LocalDate fechaApertura;           // Obligatorio - columna 0
    private String cliente;                    // Obligatorio - columna 1
    private String area;                       // Obligatorio - columna 2
    private String proyecto;                   // Obligatorio - columna 3
    private String fase;                       // Obligatorio - columna 4
    private String site;                       // Obligatorio - columna 5
    private String region;                     // Obligatorio - columna 6
    private String estado = "ASIGNACION";      // Obligatorio, siempre ASIGNACION - columna 7
    private Integer otAnterior;                // Opcional - columna 8

    // Responsables - columna 9-15
    private String jefaturaClienteSolicitante;    // Obligatorio - columna 9
    private String analistaClienteSolicitante;    // Obligatorio - columna 10
    private String coordinadorTiCw;               // Obligatorio - columna 11
    private String jefaturaResponsable;           // Obligatorio - columna 12
    private String liquidador;                    // Obligatorio - columna 13
    private String ejecutante;                    // Obligatorio - columna 14
    private String analistaContable;              // Obligatorio - columna 15

    // Getters y Setters generados automáticamente por @Data
}