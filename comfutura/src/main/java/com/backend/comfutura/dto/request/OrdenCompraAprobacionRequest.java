package com.backend.comfutura.dto.request;


import lombok.Data;


@Data
public class OrdenCompraAprobacionRequest {
    private Integer nivel;
    private String estado;   // APROBADO | RECHAZADO
    private String comentario;
}



