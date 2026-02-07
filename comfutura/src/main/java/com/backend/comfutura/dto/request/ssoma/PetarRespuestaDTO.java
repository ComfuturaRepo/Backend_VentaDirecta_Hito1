package com.backend.comfutura.dto.request.ssoma;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetarRespuestaDTO {
    private Integer preguntaId;
    private Boolean respuesta;
    private String observaciones;
}