package com.backend.comfutura.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CrearOtCompletaRequest {

    @NotNull
    @Valid
    private OtCreateRequest ot;

    @Valid
    private List<OtTrabajadorRequest> trabajadores;

    @Valid
    private List<OtDetalleRequest> detalles;
}
