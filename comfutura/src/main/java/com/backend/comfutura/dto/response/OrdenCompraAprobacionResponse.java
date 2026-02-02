package com.backend.comfutura.dto.response;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrdenCompraAprobacionResponse {
    private Integer nivel;
    private String estado;
    private String aprobadoPor;
    private String aprobadoEmail;
    private String comentario;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Integer diasEnEstado;
}

