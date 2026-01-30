package com.backend.comfutura.dto.response.permisos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermisoCargoTablaDTO {
    private Integer idCargo;
    private String nombre;
    private Boolean activo;
}