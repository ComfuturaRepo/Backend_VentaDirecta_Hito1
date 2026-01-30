package com.backend.comfutura.dto.response.permisos;

public class AreaDTO {
    private Integer idArea;
    private String nombre;
    private Boolean activo;

    // Getters y Setters
    public Integer getIdArea() { return idArea; }
    public void setIdArea(Integer idArea) { this.idArea = idArea; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}