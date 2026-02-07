package com.backend.comfutura.dto.request.ssoma;


import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaRequestDTO {

    // Datos del formulario principal
    private Integer idOts;
    private Integer empresaId;
    private Integer trabajoId;
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

    // Datos de participantes
    private List<ParticipanteDTO> participantes;

    // Secuencias de tarea
    private List<SecuenciaTareaDTO> secuenciasTarea;

    // Checklist seguridad
    private List<ChecklistSeguridadDTO> checklistSeguridad;

    // EPP checks
    private List<EppCheckDTO> eppChecks;

    // Charla
    private CharlaDTO charla;

    // Inspecciones trabajador
    private List<InspeccionTrabajadorDTO> inspeccionesTrabajador;

    // Herramientas inspección
    private List<HerramientaInspeccionDTO> herramientasInspeccion;

    // PETAR
    private PetarDTO petar;

    // Fotos y videos (se manejan separadamente)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FotoParticipante {
        private Integer participanteIndex;
        private MultipartFile foto;
        private String tipoFoto; // "FRONTAL" o "CREDENCIAL"
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class VideoCharla {
        private MultipartFile video;
        private Integer duracionSegundos;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FotoEpp {
        private Integer eppIndex;
        private MultipartFile foto;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FotoHerramienta {
        private Integer herramientaIndex;
        private MultipartFile foto;
    }

    // Listas de archivos
    private List<FotoParticipante> fotosParticipantes;
    private VideoCharla videoCharla;
    private List<FotoEpp> fotosEpp;
    private List<FotoHerramienta> fotosHerramientas;
}






