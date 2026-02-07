package com.backend.comfutura.dto.response.ssomaDTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecuenciaTareaResponseDTO {
    private String secuenciaTarea;
    private String peligro;
    private String riesgo;
    private String consecuencias;
    private String medidasControl;
}