package com.backend.comfutura.dto.request.SSOMA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapacitacionRequestDTO {
    private String tema;
    private LocalDate fecha;
    private LocalTime hora;
    private Integer idCapacitador;
    private List<AsistenteDTO> asistentes;
}





