package com.backend.comfutura.dto.response.ssomaDTO;

import com.backend.comfutura.dto.request.ssomaDTO.InspeccionHerramientaDetalleRequest;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class InspeccionHerramientaResponse {
    private Integer id;
    private String numeroRegistro;
    private LocalDate fecha;
    private String ubicacionSede;
    private Integer idSupervisor;
    private String nombreSupervisor;
    private Integer idSupervisorTrabajo;
    private String nombreSupervisorTrabajo;
    private Integer idResponsableLugar;
    private String nombreResponsableLugar;
    private Integer idSupervisorSst;
    private String nombreSupervisorSst;
}