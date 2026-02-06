package com.backend.comfutura.dto.response.permisos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermisoAreaTablaDTO {
    private Integer idArea;
    private String nombre;
    private Boolean activo;
}