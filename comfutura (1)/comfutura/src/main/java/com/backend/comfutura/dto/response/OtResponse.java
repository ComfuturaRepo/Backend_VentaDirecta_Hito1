package com.backend.comfutura.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtResponse {

    private Integer id;
    private Integer ot;
    private String ceco;

    private Integer idCliente;
    private String nombreCliente;      // ← valor útil para el frontend

    private Integer idArea;
    private String nombreArea;         // ← valor útil para el frontend

    private String descripcion;
    private LocalDate fechaApertura;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}