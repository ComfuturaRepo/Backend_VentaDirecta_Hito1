package com.backend.comfutura.dto.request;


import lombok.Data;

@Data
public class OrdenCompraRequestDTO {

    private String idEstadoOc;
    private String idOts;
    private String idMaestro;
    private String idProveedor;

    private String cantidad;
    private String costoUnitario;

    private String observacion;
}

