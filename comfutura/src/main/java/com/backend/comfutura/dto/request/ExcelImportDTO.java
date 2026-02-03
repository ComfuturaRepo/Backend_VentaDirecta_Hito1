package com.backend.comfutura.dto.request;

import com.backend.comfutura.dto.request.otDTO.OtCreateRequest;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ExcelImportDTO {
    private Integer filaExcel;
    private boolean valido = true;
    private String mensajeError;

    // Generado automáticamente al importar
    private Integer ot;

    // Temporal para guardar el request validado
    private OtCreateRequest tempRequest;

    // Campos de Excel (según plantilla)
    private LocalDate fechaApertura;           // Obligatorio
    private String cliente;                    // Obligatorio
    private String area;                       // Obligatorio
    private String proyecto;                   // Obligatorio
    private String fase;                       // Obligatorio
    private String site;                       // Obligatorio
    private String siteDescripcion;                       // Obligatorio
    private String region;                     // Obligatorio
    private String estado;                     // Obligatorio, siempre ASIGNACION
    private Integer otAnterior;                // Opcional, con límite int

    // ✅ NUEVO CAMPO: Tipo OT (obligatorio)
    private String tipoOt;                     // Obligatorio - columna 8

    // Responsables
    private String jefaturaClienteSolicitante;    // Obligatorio
    private String analistaClienteSolicitante;    // Obligatorio
    private String coordinadorTiCw;               // Obligatorio
    private String jefaturaResponsable;           // Obligatorio
    private String liquidador;                    // Obligatorio
    private String ejecutante;                    // Obligatorio
    private String analistaContable;              // Obligatorio

    // Método para validar límite de OT anterior
    public boolean esOtAnteriorValida() {
        if (otAnterior == null) {
            return true; // Es opcional
        }
        return otAnterior >= 1 && otAnterior <= 2147483647;
    }

    public String getOtAnteriorMensajeError() {
        if (otAnterior == null) {
            return "";
        }
        if (otAnterior < 1) {
            return "OT anterior debe ser mayor o igual a 1";
        }
        if (otAnterior > 2147483647) {
            return "OT anterior excede el límite máximo (2,147,483,647)";
        }
        return "";
    }
}