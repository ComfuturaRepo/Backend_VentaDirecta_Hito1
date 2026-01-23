package com.backend.comfutura.dto.request;


import lombok.Data;

@Data
public class TrabajadorRequestDTO {

    private Integer idTrabajador; // null = crear | no null = editar

    private String nombres;
    private String apellidos;
    private String dni;
    private String celular;
    private String correoCorporativo;

    private Integer idEmpresa;
    private Integer idArea;
    private Integer idCargo;
}