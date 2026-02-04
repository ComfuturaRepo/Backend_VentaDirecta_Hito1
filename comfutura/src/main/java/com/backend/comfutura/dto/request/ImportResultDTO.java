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
    private String mensajeResumen;
    private List<String> erroresDetallados = new ArrayList<>();
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
        if (fallidos == 0 && erroresValidacion == 0) {
            this.mensajeResumen = String.format(
                    "✅ IMPORTACIÓN COMPLETADA: %d registros procesados exitosamente",
                    exitosos
            );
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("⚠️ IMPORTACIÓN CON ERRORES\n\n");
            sb.append(String.format("Exitosos: %d\n", exitosos));
            sb.append(String.format("Fallidos: %d\n", fallidos));

            if (erroresValidacion > 0) {
                sb.append(String.format("Errores de validación: %d\n", erroresValidacion));
            }
            if (erroresPersistencia > 0) {
                sb.append(String.format("Errores de persistencia: %d\n", erroresPersistencia));
            }

            this.mensajeResumen = sb.toString();
        }
    }

    // Método para agregar errores detallados
    public void agregarErrorDetallado(int fila, String error) {
        erroresDetallados.add("Fila " + fila + ": " + error);
    }

    // Método para obtener errores como texto
    public String getErroresComoTexto() {
        if (erroresDetallados.isEmpty()) {
            return "No hay errores detallados.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ERRORES DETALLADOS:\n\n");
        for (String error : erroresDetallados) {
            sb.append(error).append("\n");
        }
        return sb.toString();
    }
}