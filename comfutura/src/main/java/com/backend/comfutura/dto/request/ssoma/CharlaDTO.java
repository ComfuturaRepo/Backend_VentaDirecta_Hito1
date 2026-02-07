package com.backend.comfutura.dto.request.ssoma;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharlaDTO {
    private Integer temaId;
    private LocalDateTime fechaCharla;
    private BigDecimal duracionHoras;
    private Integer capacitadorId;
}