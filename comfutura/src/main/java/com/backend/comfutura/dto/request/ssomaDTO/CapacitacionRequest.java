package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CapacitacionRequest {
    private String numeroRegistro;
    private String tema;
    private LocalDate fecha;
    private LocalTime hora;
    private Integer idCapacitador;
    private List<CapacitacionAsistenteRequest> asistentes;
}

