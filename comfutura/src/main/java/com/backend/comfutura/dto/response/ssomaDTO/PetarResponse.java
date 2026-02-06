package com.backend.comfutura.dto.response.ssomaDTO;

import com.backend.comfutura.dto.request.ssomaDTO.PetarRespuestaRequest;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
@Data
public class PetarResponse {
    private Integer id;
    private String numeroRegistro;
    private LocalDate fecha;
    private Boolean requiereEvaluacionAmbiente;
    private Boolean aperturaLineaEquipos;
    private LocalTime horaInicio;
    private String recursosNecesarios;
    private String procedimiento;
    private Integer idSupervisorTrabajo;
    private String nombreSupervisorTrabajo;
    private Integer idResponsableLugar;
    private String nombreResponsableLugar;
    private Integer idSupervisorSst;
    private String nombreSupervisorSst;
    private Integer idBrigadista;
    private String nombreBrigadista;
    private Integer idResponsableTrabajo;
    private String nombreResponsableTrabajo;
}