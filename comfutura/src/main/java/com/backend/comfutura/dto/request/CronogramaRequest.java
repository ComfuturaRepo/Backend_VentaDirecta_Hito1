package com.backend.comfutura.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CronogramaRequest {

    // 🔹 OT a la que pertenece
    private Integer idOts;

    // 🔹 Maestro Partida (seleccionado desde combo)
    private Integer idMaestroPartida;

    // 🔹 Datos del cronograma
    private BigDecimal duracionDias;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}

