package com.backend.comfutura.dto.request;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @NotNull(message = "El trabajador es obligatorio")
    private Integer trabajadorId;

    @NotNull(message = "El nivel es obligatorio")
    private Integer nivelId;
}