package com.backend.comfutura.dto.response.ssomaDTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharlaResponseDTO {
    private String temaNombre;
    private LocalDateTime fechaCharla;
    private BigDecimal duracionHoras;
    private String capacitadorNombre;
    private String videoUrl;
    private Integer duracionSegundos;
}
