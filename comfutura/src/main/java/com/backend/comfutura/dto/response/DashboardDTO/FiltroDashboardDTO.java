package com.backend.comfutura.dto.response.DashboardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroDashboardDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer clienteId;
    private Integer areaId;
    private Integer proyectoId;
    private Integer estadoId;
    private Integer regionId;
    private Integer siteId;
    private Integer faseId;
    private Integer trabajadorId;

    // Constructor para filtro por rango de fechas
    public FiltroDashboardDTO(LocalDate fechaInicio, LocalDate fechaFin) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    // Constructor para filtro por cliente
    public FiltroDashboardDTO(Integer clienteId) {
        this.clienteId = clienteId;
    }

    // Constructor para filtro por área
    public FiltroDashboardDTO(Integer areaId, boolean isArea) {
        this.areaId = areaId;
    }

    // Constructor para filtro por proyecto
    public FiltroDashboardDTO(Integer proyectoId, String tipo) {
        this.proyectoId = proyectoId;
    }

    // Métodos de validación
    public boolean tieneFiltroFecha() {
        return fechaInicio != null && fechaFin != null;
    }

    public boolean tieneFiltroCliente() {
        return clienteId != null;
    }

    public boolean tieneFiltroArea() {
        return areaId != null;
    }

    public boolean tieneFiltroProyecto() {
        return proyectoId != null;
    }

    public boolean tieneFiltroEstado() {
        return estadoId != null;
    }

    public boolean tieneFiltros() {
        return fechaInicio != null || fechaFin != null || clienteId != null ||
                areaId != null || proyectoId != null || estadoId != null ||
                regionId != null || siteId != null || faseId != null ||
                trabajadorId != null;
    }
}