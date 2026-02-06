package com.backend.comfutura.dto.response.DashboardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumenCostosDTO {
    private BigDecimal costoMateriales;
    private BigDecimal costoContratistas;
    private BigDecimal costoGastosLogisticos;
    private BigDecimal costoViaticos;
    private BigDecimal costoPlanillas;
    private BigDecimal costoTotal;
    private BigDecimal porcentajeMateriales;
    private BigDecimal porcentajeContratistas;
    private BigDecimal porcentajeGastosLogisticos;
    private BigDecimal porcentajeViaticos;
    private BigDecimal porcentajePlanillas;
}