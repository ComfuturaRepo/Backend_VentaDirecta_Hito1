package com.backend.comfutura.dto.request.ssoma;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecuenciaTareaDTO {
    private String secuenciaTarea;
    private String peligro;
    private String riesgo;
    private String consecuencias;
    private String medidasControl;
    private Integer orden;
}