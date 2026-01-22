package com.backend.comfutura.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtTrabajadorRequest {

    private Integer idTrabajador;
    private String nombres;          // opcional: para mostrar nombre completo en frontend
    private String apellidos;        // opcional: para mostrar nombre completo
    private String dni;              // opcional: útil para identificación
    private String rolEnOt;
}
