package com.backend.comfutura.dto.request;
import lombok.Data;
import jakarta.validation.constraints.*;

@Data
public class UsuarioUpdateDTO {

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    private String username;

    @NotNull(message = "El trabajador es obligatorio")
    private Integer trabajadorId;

    @NotNull(message = "El nivel es obligatorio")
    private Integer nivelId;


}