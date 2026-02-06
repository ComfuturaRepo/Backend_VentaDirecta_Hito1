package com.backend.comfutura.dto.response.DashboardDTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOTDTO {
    private EstadisticasGeneralesDTO generales;
    private List<OTPorEstadoDTO> otsPorEstado;
    private List<OTPorClienteDTO> otsPorCliente;
    private List<OTPorAreaDTO> otsPorArea;
    private List<OTPorProyectoDTO> otsPorProyecto;
    private List<OTPorRegionDTO> otsPorRegion;
    private EvolucionMensualDTO evolucionMensual;
    private ResumenCostosDTO resumenCostos;
    private List<OTPendienteDTO> otsPendientes;
}