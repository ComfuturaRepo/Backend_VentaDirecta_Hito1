package com.backend.comfutura.dto.response;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UsuarioDetailDTO {
    private Integer idUsuario;
    private String username;
    private Integer trabajadorId;
    private String trabajadorNombre;
    private String trabajadorApellidos;
    private String trabajadorDNI;
    private Integer nivelId;
    private String nivelNombre;
    private String nivelCodigo;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}