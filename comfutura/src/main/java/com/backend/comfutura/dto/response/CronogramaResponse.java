package com.backend.comfutura.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CronogramaResponse {

    private Integer idCronograma;   //  FALTABA
    private String partida;
    private BigDecimal duracionDias;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
