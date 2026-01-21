package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtTrabajadorResponse {
    private Integer idTrabajador;   // único dato necesario para identificar
    private String rolEnOt;         // el rol que tiene en esta OT
}