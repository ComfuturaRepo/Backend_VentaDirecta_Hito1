package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;

@Data
public class AtsRiesgoRequest {
    private Integer idTarea;
    private Integer idPeligro;
    private Integer idRiesgo;
    private Integer idMedida;
private String observacion;
}