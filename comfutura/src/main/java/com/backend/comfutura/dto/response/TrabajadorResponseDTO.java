package com.backend.comfutura.dto.response;



import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TrabajadorResponseDTO {

    private Integer idTrabajador;

    private String nombres;
    private String apellidos;
    private String dni;
    private String celular;
    private String correoCorporativo;

    private String empresa;
    private String area;
    private String cargo;

    private Boolean activo;
    private LocalDateTime fechaCreacion;
}