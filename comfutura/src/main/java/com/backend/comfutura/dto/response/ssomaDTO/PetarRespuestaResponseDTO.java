package com.backend.comfutura.dto.response.ssomaDTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetarRespuestaResponseDTO {
    private String pregunta;
    private Boolean respuesta;
    private String observaciones;
}