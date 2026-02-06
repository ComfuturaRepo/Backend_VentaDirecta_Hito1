package com.backend.comfutura.dto.response.permisos;


import com.backend.comfutura.dto.request.areaDTO.AreaDTO;

import java.util.ArrayList;
import java.util.List;

public class PermisoResponseDTO {
    private Integer idPermiso;
    private String codigo;
    private String nombre;
    private String descripcion;
    private List<NivelDTO> niveles = new ArrayList<>();
    private List<AreaDTO> areas = new ArrayList<>();
    private List<CargoDTO> cargos = new ArrayList<>();

    // Getters y Setters
    public Integer getIdPermiso() { return idPermiso; }
    public void setIdPermiso(Integer idPermiso) { this.idPermiso = idPermiso; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<NivelDTO> getNiveles() { return niveles; }
    public void setNiveles(List<NivelDTO> niveles) { this.niveles = niveles; }

    public List<AreaDTO> getAreas() { return areas; }
    public void setAreas(List<AreaDTO> areas) { this.areas = areas; }

    public List<CargoDTO> getCargos() { return cargos; }
    public void setCargos(List<CargoDTO> cargos) { this.cargos = cargos; }
}