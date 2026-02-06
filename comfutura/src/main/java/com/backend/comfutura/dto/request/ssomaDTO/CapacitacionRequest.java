package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CapacitacionRequest {
    private String tema;
    private String tipoCharla;
    private Integer idOts;
    private Integer idCapacitador;
    private Integer idSupervisorTrabajo;
    private Integer idResponsableLugar;
    private Integer idSupervisorSst;
    private List<CapacitacionAsistenteRequest> asistentes;
}
