package com.backend.comfutura.dto.response.DashboardDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPendienteDTO {
    private Integer ot;
    private String cliente;
    private String descripcion;
    private String estado;
    private LocalDate fechaApertura;
    private Integer diasPendientes;
    private BigDecimal costoEstimado;  // Debe ser BigDecimal
    private String responsable;
}