package com.backend.comfutura.dto.request.ssomaDTO;


import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class InspeccionHerramientaRequest {
    private String ubicacionSede;
    private Integer idOts;
    private Integer idCliente;
    private Integer idProyecto;
    private Integer idSupervisor;
    private Integer idSupervisorTrabajo;
    private Integer idResponsableLugar;
    private Integer idSupervisorSst;
    private List<InspeccionHerramientaDetalleRequest> detalles;
}
