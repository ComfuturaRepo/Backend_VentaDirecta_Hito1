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
    private List<ExcelImportDTO> registrosProcesados = new ArrayList<>();
    private List<ExcelImportDTO> registrosConError = new ArrayList<>();

    public void incrementarExitosos() {
        this.exitosos++;
    }

    public void incrementarFallidos() {
        this.fallidos++;
    }

    public void agregarRegistroProcesado(ExcelImportDTO registro) {
        this.registrosProcesados.add(registro);
    }

    public void agregarRegistroConError(ExcelImportDTO registro) {
        this.registrosConError.add(registro);
        incrementarFallidos();
    }
}