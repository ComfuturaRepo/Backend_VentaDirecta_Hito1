package com.backend.comfutura.dto.response.DashboardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadisticasGeneralesDTO {
    private Long totalOTs;
    private Long otsActivas;
    private Long otsCompletadas;
    private Long otsPendientes;
    private Long otsCanceladas;
    private BigDecimal costoTotalOTs;
    private BigDecimal costoPromedioOT;
    private Long otsEsteMes;
    private Long otsEsteAnio;
}