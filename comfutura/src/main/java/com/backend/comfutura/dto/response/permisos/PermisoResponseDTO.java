package com.backend.comfutura.dto.response.permisos;


import com.backend.comfutura.dto.request.areaDTO.AreaDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PermisoResponseDTO {
    private Integer idPermiso;
    private String codigo;
    private String nombre;
    private String descripcion;
    private List<NivelDTO> niveles = new ArrayList<>();
    private List<AreaDTO> areas = new ArrayList<>();
    private List<CargoDTO> cargos = new ArrayList<>();
    private List<TrabajadorDTO> trabajadores = new ArrayList<>(); // NUEVO


}