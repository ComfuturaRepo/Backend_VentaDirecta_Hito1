package com.backend.comfutura.dto.response.permisos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermisoNivelTablaDTO {
    private Integer idNivel;
    private String codigo;
    private String nombre;
}