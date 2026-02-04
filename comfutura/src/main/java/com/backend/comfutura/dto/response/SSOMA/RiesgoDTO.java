package com.backend.comfutura.dto.response.SSOMA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiesgoDTO {
    private String tarea;
    private String peligro;
    private String riesgo;
    private String medida;
}