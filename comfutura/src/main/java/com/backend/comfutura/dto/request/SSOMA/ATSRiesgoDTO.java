package com.backend.comfutura.dto.request.SSOMA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATSRiesgoDTO {
    private Integer idTarea;
    private Integer idPeligro;
    private Integer idRiesgo;
    private Integer idMedida;
}