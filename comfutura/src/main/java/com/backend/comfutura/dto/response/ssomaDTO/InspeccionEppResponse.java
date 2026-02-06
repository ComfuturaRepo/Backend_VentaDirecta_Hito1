package com.backend.comfutura.dto.response.ssomaDTO;

import com.backend.comfutura.dto.request.ssomaDTO.InspeccionEppDetalleRequest;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class InspeccionEppResponse {
    private Integer id;
    private String numeroRegistro;
    private LocalDate fecha;
    private String tipoInspeccion;
    private String areaTrabajo;
    private Integer idInspector;
    private String nombreInspector;
    private Integer idSupervisorTrabajo;
    private String nombreSupervisorTrabajo;
    private Integer idResponsableLugar;
    private String nombreResponsableLugar;
    private Integer idSupervisorSst;
    private String nombreSupervisorSst;
}
