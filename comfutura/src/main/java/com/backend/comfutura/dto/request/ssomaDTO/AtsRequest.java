package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AtsRequest {
    private LocalDate fecha;
    private LocalTime hora;
    private String empresa;
    private String lugarTrabajo;
    private Integer idTrabajo;
    private List<AtsParticipanteRequest> participantes;
    private List<AtsRiesgoRequest> riesgos;
    private List<Integer> eppIds;
    private List<Integer> tipoRiesgoIds;
}


