package com.backend.comfutura.dto.request;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class ImportResultDTO {
    private Long inicio;
    private Long fin;
    private Long duracionMs;
    private Integer totalRegistros;
    private Integer exitosos = 0;
    private Integer fallidos = 0;
    private boolean exito;
    private String mensaje;

    // Estadísticas detalladas
    private Integer erroresValidacion = 0;
    private Integer erroresPersistencia = 0;
    private Integer warnings = 0;

    private List<ExcelImportDTO> registrosProcesados = new ArrayList<>();
    private List<ExcelImportDTO> registrosConError = new ArrayList<>();
    private List<String> resumenErrores = new ArrayList<>();

    public void incrementarExitosos() {
        this.exitosos++;
    }

    public void incrementarFallidos() {
        this.fallidos++;
    }

    public void incrementarErroresValidacion() {
        this.erroresValidacion++;
    }

    public void incrementarErroresPersistencia() {
        this.erroresPersistencia++;
    }

    public void incrementarWarnings() {
        this.warnings++;
    }

    public void agregarResumenError(String error) {
        if (!this.resumenErrores.contains(error)) {
            this.resumenErrores.add(error);
        }
    }

    public void agregarRegistroProcesado(ExcelImportDTO registro) {
        this.registrosProcesados.add(registro);
    }

    public void agregarRegistroConError(ExcelImportDTO registro) {
        this.registrosConError.add(registro);
        incrementarFallidos();
    }

    public void generarMensajeResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("Proceso completado: ");
        sb.append(exitosos).append(" éxitos, ");
        sb.append(fallidos).append(" fallidos");

        if (erroresValidacion > 0) {
            sb.append(", ").append(erroresValidacion).append(" errores de validación");
        }
        if (erroresPersistencia > 0) {
            sb.append(", ").append(erroresPersistencia).append(" errores de persistencia");
        }
        if (warnings > 0) {
            sb.append(", ").append(warnings).append(" advertencias");
        }

        this.mensaje = sb.toString();
    }
}