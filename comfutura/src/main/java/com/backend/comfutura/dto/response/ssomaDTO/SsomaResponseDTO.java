package com.backend.comfutura.dto.response.ssomaDTO;

import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaResponseDTO {

    private Integer idSsoma;
    private Integer idOts;
    private String empresaNombre;
    private String trabajoNombre;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private LocalDateTime fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private LocalTime horaInicioTrabajo;
    private LocalTime horaFinTrabajo;
    private String supervisorTrabajo;
    private String supervisorSst;
    private String responsableArea;

    // Participantes
    private List<ParticipanteResponseDTO> participantes;

    // Secuencias
    private List<SecuenciaTareaResponseDTO> secuenciasTarea;

    // Checklist
    private List<ChecklistSeguridadResponseDTO> checklistSeguridad;

    // EPP
    private List<EppCheckResponseDTO> eppChecks;

    // Charla
    private CharlaResponseDTO charla;

    // Inspecciones
    private List<InspeccionTrabajadorResponseDTO> inspeccionesTrabajador;

    // Herramientas
    private List<HerramientaInspeccionResponseDTO> herramientasInspeccion;

    // PETAR
    private PetarResponseDTO petar;
}