package com.backend.comfutura.dto.response;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TrabajadorDetailDTO {
    private Integer idTrabajador;
    private String nombres;
    private String apellidos;
    private String dni;
    private String celular;
    private String correoCorporativo;
    private Integer empresaId;
    private String empresaNombre;
    private Integer areaId;
    private String areaNombre;
    private Integer cargoId;
    private String cargoNombre;
    private String cargoNivel;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    // Helper para nombre completo
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}