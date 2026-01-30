package com.backend.comfutura.dto.response.permisos;

public class CargoDTO {
    private Integer idCargo;
    private String nombre;
    private Integer idNivel;
    private Boolean activo;
    private String nombreNivel; // Para mostrar en frontend

    // Getters y Setters
    public Integer getIdCargo() { return idCargo; }
    public void setIdCargo(Integer idCargo) { this.idCargo = idCargo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getIdNivel() { return idNivel; }
    public void setIdNivel(Integer idNivel) { this.idNivel = idNivel; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public String getNombreNivel() { return nombreNivel; }
    public void setNombreNivel(String nombreNivel) { this.nombreNivel = nombreNivel; }
}