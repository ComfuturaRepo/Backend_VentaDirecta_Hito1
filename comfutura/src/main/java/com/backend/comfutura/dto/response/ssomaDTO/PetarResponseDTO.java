package com.backend.comfutura.dto.response.ssomaDTO;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetarResponseDTO {
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
    private List<PetarRespuestaResponseDTO> respuestas;
    private List<EquipoProteccionResponseDTO> equiposProteccion;
}
