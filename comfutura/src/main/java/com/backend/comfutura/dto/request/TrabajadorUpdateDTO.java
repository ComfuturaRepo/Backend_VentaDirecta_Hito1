package com.backend.comfutura.dto.request;

import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class TrabajadorUpdateDTO {

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 100, message = "Los nombres deben tener entre 2 y 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 100, message = "Los apellidos deben tener entre 2 y 100 caracteres")
    private String apellidos;

    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @Pattern(regexp = "^[0-9]{9}$", message = "El celular debe tener 9 dígitos")
    private String celular;

    @Email(message = "El correo debe ser válido")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    private String correoCorporativo;

    @NotNull(message = "El área es obligatoria")
    private Integer areaId;

    @NotNull(message = "El cargo es obligatorio")
    private Integer cargoId;

    private Integer empresaId;
}