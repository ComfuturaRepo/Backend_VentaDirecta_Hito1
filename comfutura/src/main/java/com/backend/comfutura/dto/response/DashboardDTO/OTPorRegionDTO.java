package com.backend.comfutura.dto.response.DashboardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPorRegionDTO {
    private String region;
    private Long cantidad;
    private BigDecimal porcentaje;
    private BigDecimal costoTotal;
}