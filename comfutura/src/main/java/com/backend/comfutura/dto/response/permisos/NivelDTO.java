package com.backend.comfutura.dto.response.permisos;

public class NivelDTO {
    private Integer idNivel;
    private String codigo;
    private String nombre;
    private String descripcion;

    // Getters y Setters
    public Integer getIdNivel() { return idNivel; }
    public void setIdNivel(Integer idNivel) { this.idNivel = idNivel; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}