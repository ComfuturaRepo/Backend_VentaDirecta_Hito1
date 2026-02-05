package com.backend.comfutura.dto.response.ssomaDTO;

import com.backend.comfutura.dto.request.ssomaDTO.AtsParticipanteRequest;
import com.backend.comfutura.dto.request.ssomaDTO.AtsRiesgoRequest;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class AtsResponse {
    private Integer idAts;
    private LocalDate fecha;
    private LocalTime hora;
    private String empresa;
    private String lugarTrabajo;
    private Integer idTrabajo;
    private String nombreTrabajo;
    private List<AtsParticipanteRequest> participantes;
    private List<AtsRiesgoRequest> riesgos;
    private List<Integer> eppIds;
    private List<Integer> tipoRiesgoIds;
}