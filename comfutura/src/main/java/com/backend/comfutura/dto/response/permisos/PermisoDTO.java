package com.backend.comfutura.dto.response.permisos;


import lombok.Data;

import java.util.List;

@Data
public class PermisoDTO {
    private Integer idPermiso;
    private String codigo;
    private String nombre;
    private String descripcion;
    private List<Integer> nivelesIds;
    private List<Integer> areasIds;
    private List<Integer> cargosIds;
    private List<Integer> trabajadoresIds;

}