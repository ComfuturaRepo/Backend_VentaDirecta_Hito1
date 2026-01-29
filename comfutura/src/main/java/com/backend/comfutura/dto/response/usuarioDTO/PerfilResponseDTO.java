package com.backend.comfutura.dto.response.usuarioDTO;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PerfilResponseDTO {

    private Integer idUsuario;
    private String username;

    // Datos del trabajador
    private Integer idTrabajador;
    private String nombres;
    private String apellidos;
    private String dni;
    private String correoCorporativo;
    private String celular;

    // Datos del nivel/rol
    private Integer idNivel;
    private String nivelNombre;
    private String nivelCodigo;

    // Información del empleo
    private String empresaNombre;
    private String areaNombre;
    private String cargoNombre;

    // Estado
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime ultimaConexion;

    // Estadísticas
    private Long totalProyectos;
    private Long tareasPendientes;
    private Long tareasCompletadas;
}