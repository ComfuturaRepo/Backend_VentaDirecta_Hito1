package com.backend.comfutura.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OtCreateRequest {

    private Integer idOtsAnterior;
    private Integer idOts;

    private Integer idCliente;
    private Integer idArea;
    private Integer idProyecto;
    private Integer idFase;
    private Integer idSite;
    private Integer idRegion;
    private LocalDate fechaApertura;

    private String descripcion;

    private Integer idJefaturaClienteSolicitante;
    private Integer idAnalistaClienteSolicitante;
    private Integer idCoordinadorTiCw;
    private Integer idJefaturaResponsable;
    private Integer idLiquidador;
    private Integer idEjecutante;
    private Integer idAnalistaContable;
    private boolean activo;
}