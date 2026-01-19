package com.backend.comfutura.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OtCreateRequest {

    @NotNull(message = "El número de OT es obligatorio")
    @Min(value = 1, message = "El número de OT debe ser mayor a 0")
    private Integer ot;

    @NotBlank(message = "El CECO es obligatorio")
    @Size(max = 20, message = "El CECO no puede exceder 20 caracteres")
    private String ceco;

    @NotNull(message = "El cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "El área es obligatoria")
    private Integer idArea;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha de apertura es obligatoria")
    @PastOrPresent(message = "La fecha de apertura no puede ser futura")
    private LocalDate fechaApertura;

}