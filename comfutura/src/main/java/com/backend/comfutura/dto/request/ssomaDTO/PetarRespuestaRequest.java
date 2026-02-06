package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;


@Data
public class PetarRespuestaRequest {
    private Integer idPregunta;
    private Boolean respuesta;
    private String observacion;
}