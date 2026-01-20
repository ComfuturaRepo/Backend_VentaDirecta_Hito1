package com.backend.comfutura.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OtCreateRequest {

    private Integer idOtsAnterior;

    private Integer idCliente;
    private Integer idArea;
    private Integer idProyecto;
    private Integer idFase;
    private Integer idSite;
    private Integer idRegion;

    private String descripcion;
    private LocalDate fechaApertura;
    private Integer diasAsignados;
}
