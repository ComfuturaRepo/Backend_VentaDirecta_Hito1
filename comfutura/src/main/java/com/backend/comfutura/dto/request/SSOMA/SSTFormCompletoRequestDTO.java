package com.backend.comfutura.dto.request.SSOMA;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SSTFormCompletoRequestDTO {
    // Datos generales para todas las hojas
    private LocalDate fecha;
    private LocalTime hora;
    private String empresa;
    private String lugarTrabajo;

    // HOJA 1: ATS
    private Integer idTrabajo;
    private List<ParticipanteRolDTO> participantes;
    private List<RiesgoCompletoDTO> riesgos;
    private List<Integer> eppIds;
    private List<Integer> tipoRiesgoIds;

    // HOJA 2: Capacitación
    private String temaCapacitacion;
    private Integer idCapacitador;
    private List<AsistenteDTO> asistentesCapacitacion;

    // HOJA 3: Inspección EPP
    private Integer idInspector;
    private List<EPPInspeccionDTO> inspeccionesEPP;

    // HOJA 4: Inspección Herramientas
    private Integer idSupervisor;
    private List<HerramientaInspeccionDTO> inspeccionesHerramientas;

    // HOJA 5: PETAR
    private List<PETARRespuestaDTO> respuestasPETAR;
    private List<Integer> trabajadoresAutorizadosPETAR}
