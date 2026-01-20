package com.backend.comfutura.dto.response;


import lombok.Data;

@Data
public class OrdenCompraResponseDTO {
    private String idOc;
    private String estado;
    private String ots;
    private String maestro;
    private String proveedor;
    private String cantidad;
    private String costoUnitario;
    private String fechaOc;
    private String observacion;
}

