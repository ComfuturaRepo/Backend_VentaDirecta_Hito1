package com.backend.comfutura.dto.response.DashboardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvolucionMensualDTO {
    private List<MesDTO> meses;
    private List<BigDecimal> costos;
    private List<Long> cantidadOTs;
}

