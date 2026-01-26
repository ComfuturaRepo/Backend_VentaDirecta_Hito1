package com.backend.comfutura.dto.response;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TrabajadorSimpleDTO {
    private Integer idTrabajador;
    private String nombres;
    private String apellidos;
    private String dni;
    private String celular;
    private String correoCorporativo;
    private String empresaNombre;
    private String areaNombre;
    private String cargoNombre;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    // Helper para nombre completo
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}