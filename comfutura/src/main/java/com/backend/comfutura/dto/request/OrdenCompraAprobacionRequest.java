package com.backend.comfutura.dto.request;


import lombok.Data;

@Data
public class OrdenCompraAprobacionRequest {
    private String aprobadoPor;
    private String aprobadoEmail;
    private String comentario;
}


