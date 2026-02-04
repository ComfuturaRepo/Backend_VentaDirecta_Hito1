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
public class ATSRequestDTO {
    private LocalDate fecha;
    private LocalTime hora;
    private String empresa;
    private String lugarTrabajo;
    private Integer idTrabajo;
    private List<ATSParticipanteDTO> participantes;
    private List<ATSRiesgoDTO> riesgos;
    private List<Integer> eppIds;
    private List<Integer> tipoRiesgoIds;
}