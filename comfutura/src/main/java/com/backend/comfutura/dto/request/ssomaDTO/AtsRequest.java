package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;
import java.util.List;

@Data
public class AtsRequest {
    private String empresa;
    private String lugarTrabajo;
    private String coordenadas;
    private Integer idOts;
    private Integer idTrabajo;
    private Integer idSupervisorTrabajo;
    private Integer idResponsableLugar;
    private Integer idSupervisorSst;
    private List<AtsParticipanteRequest> participantes;
    private List<AtsRiesgoRequest> riesgos;  // Cambiado de Integer a List
    private List<Integer> eppIds;  // Faltaba esto
    private List<Integer> tipoRiesgoIds;  // Faltaba esto
}