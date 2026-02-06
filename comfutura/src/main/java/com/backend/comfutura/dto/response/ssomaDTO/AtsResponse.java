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
    private String numeroRegistro;
    private String empresa;
    private String lugarTrabajo;
    private String coordenadas;
    private Integer idOts;
    private Integer codigoOt;
    private Integer idSupervisorTrabajo;
    private String nombreSupervisorTrabajo;
    private Integer idResponsableLugar;
    private String nombreResponsableLugar;
    private Integer idSupervisorSst;
    private String nombreSupervisorSst;
    private Integer idTrabajo;
    private String nombreTrabajo;
}