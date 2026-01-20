package com.backend.comfutura.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtResponse {

    private Integer idOts;
    private Integer ot;

    private String descripcion;
    private LocalDate fechaApertura;
    private Integer diasAsignados;

    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
