package com.backend.comfutura.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioSimpleDTO {
    private Integer idUsuario;
    private String username;
    private String nombreTrabajador;
    private String nivelNombre;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}