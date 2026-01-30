package com.backend.comfutura.dto.response.permisos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermisoTablaDTO {
    private Integer idPermiso;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Boolean activo;
    private List<PermisoNivelTablaDTO> niveles;
    private List<PermisoAreaTablaDTO> areas;
    private List<PermisoCargoTablaDTO> cargos;
}




