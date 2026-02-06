package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class InspeccionEppRequest {
    private String tipoInspeccion;
    private String areaTrabajo;
    private Integer idOts;
    private Integer idInspector;
    private Integer idSupervisorTrabajo;
    private Integer idResponsableLugar;
    private Integer idSupervisorSst;
    private List<InspeccionEppDetalleRequest> detalles;
}