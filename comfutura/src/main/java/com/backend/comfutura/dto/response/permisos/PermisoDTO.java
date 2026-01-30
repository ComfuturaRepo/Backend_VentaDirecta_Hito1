package com.backend.comfutura.dto.response.permisos;


import java.util.List;

public class PermisoDTO {
    private Integer idPermiso;
    private String codigo;
    private String nombre;
    private String descripcion;
    private List<Integer> nivelesIds;
    private List<Integer> areasIds;
    private List<Integer> cargosIds;

    // Getters y Setters
    public Integer getIdPermiso() { return idPermiso; }
    public void setIdPermiso(Integer idPermiso) { this.idPermiso = idPermiso; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<Integer> getNivelesIds() { return nivelesIds; }
    public void setNivelesIds(List<Integer> nivelesIds) { this.nivelesIds = nivelesIds; }

    public List<Integer> getAreasIds() { return areasIds; }
    public void setAreasIds(List<Integer> areasIds) { this.areasIds = areasIds; }

    public List<Integer> getCargosIds() { return cargosIds; }
    public void setCargosIds(List<Integer> cargosIds) { this.cargosIds = cargosIds; }
}