package com.backend.comfutura.dto.Page.sitePage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DescripcionDTO {

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 1, max = 500, message = "La descripción debe tener entre 1 y 500 caracteres")
    private String descripcion;

    @Size(max = 500, message = "La dirección no puede exceder los 500 caracteres")
    private String direccion;

    private Boolean activo;
}