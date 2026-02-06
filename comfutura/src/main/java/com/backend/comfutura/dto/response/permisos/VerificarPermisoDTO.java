package com.backend.comfutura.dto.response.permisos;

public class VerificarPermisoDTO {
    private String codigoPermiso;
    private Integer idUsuario; // o el identificador del usuario logueado

    // Getters y Setters
    public String getCodigoPermiso() { return codigoPermiso; }
    public void setCodigoPermiso(String codigoPermiso) { this.codigoPermiso = codigoPermiso; }

    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
}