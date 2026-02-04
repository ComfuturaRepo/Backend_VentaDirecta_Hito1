package com.backend.comfutura.dto.response.SSOMA;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATSResponseDTO {
    private Integer idATS;
    private LocalDate fecha;
    private LocalTime hora;
    private String empresa;
    private String lugarTrabajo;
    private TrabajoDTO trabajo;
    private List<ParticipanteDTO> participantes;
    private List<RiesgoDTO> riesgos;
    private List<EPPDTO> epps;
    private List<TipoRiesgoDTO> tiposRiesgo;
}





