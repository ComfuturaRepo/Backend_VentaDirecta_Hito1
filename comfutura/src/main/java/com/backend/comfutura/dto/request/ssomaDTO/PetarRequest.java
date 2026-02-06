package com.backend.comfutura.dto.request.ssomaDTO;


import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PetarRequest {
    private Boolean requiereEvaluacionAmbiente;
    private Boolean aperturaLineaEquipos;
    private LocalTime horaInicio;
    private String recursosNecesarios;
    private String procedimiento;
    private Integer idOts;
    private Integer idSupervisorTrabajo;
    private Integer idResponsableLugar;
    private Integer idSupervisorSst;
    private Integer idBrigadista;
    private Integer idResponsableTrabajo;
    private Boolean conformidadRequerida;
    private List<PetarRespuestaRequest> respuestas;
    private List<Integer> trabajadoresAutorizadosIds;
}