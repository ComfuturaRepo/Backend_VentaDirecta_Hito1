package com.backend.comfutura.dto.response.permisos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermisoTrabajadorTablaDTO {
    private Integer idTrabajador;
    private String nombres;
    private String apellidos;
    private String dni;
    private Boolean activo;
}