package com.backend.comfutura.dto.response.ssomaDTO;
import com.backend.comfutura.dto.request.ssomaDTO.CapacitacionAsistenteRequest;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CapacitacionResponse {
    private Integer idCapacitacion;
    private String numeroRegistro;
    private String tema;
    private LocalDate fecha;
    private LocalTime hora;
    private String tipoCharla;
    private Integer idCapacitador;
    private String nombreCapacitador;
    private Integer idSupervisorTrabajo;
    private String nombreSupervisorTrabajo;
    private Integer idResponsableLugar;
    private String nombreResponsableLugar;
    private Integer idSupervisorSst;
    private String nombreSupervisorSst;
}