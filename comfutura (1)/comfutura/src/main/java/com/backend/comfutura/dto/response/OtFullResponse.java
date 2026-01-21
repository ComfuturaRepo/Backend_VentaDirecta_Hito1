package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OtFullResponse {

    // Datos principales de la OT
    private Integer idOts;
    private Integer ot;                      // número de OT generado
    private Integer idOtsAnterior;

    // Referencias (IDs para formularios de edición)
    private Integer idCliente;
    private Integer idArea;
    private Integer idProyecto;
    private Integer idFase;
    private Integer idSite;
    private Integer idRegion;

    // Campos descriptivos
    private String descripcion;
    private LocalDate fechaApertura;

    // Responsables (IDs)
    private Integer idJefaturaClienteSolicitante;
    private Integer idAnalistaClienteSolicitante;
    private Integer idCoordinadorTiCw;
    private Integer idJefaturaResponsable;
    private Integer idLiquidador;
    private Integer idEjecutante;
    private Integer idAnalistaContable;

    // Estado
    private Boolean activo;
    private LocalDateTime fechaCreacion;

}