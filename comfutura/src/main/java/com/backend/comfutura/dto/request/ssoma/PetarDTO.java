package com.backend.comfutura.dto.request.ssoma;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetarDTO {
    private Boolean energiaPeligrosa;
    private Boolean trabajoAltura;
    private Boolean izaje;
    private Boolean excavacion;
    private Boolean espaciosConfinados;
    private Boolean trabajoCaliente;
    private Boolean otros;
    private String otrosDescripcion;
    private String velocidadAire;
    private String contenidoOxigeno;
    private LocalTime horaInicioPetar;
    private LocalTime horaFinPetar;
    private List<PetarRespuestaDTO> respuestas;
    private List<EquipoProteccionDTO> equiposProteccion;
}