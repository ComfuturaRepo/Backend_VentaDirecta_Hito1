// OtFullResponse.java  →  para edición (solo IDs + campos editables)
package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class OtFullResponse {
    private Integer idOts;
    private Integer ot;
    private Integer idOtsAnterior;

    private Integer idCliente;
    private Integer idArea;
    private Integer idProyecto;
    private Integer idFase;
    private Integer idSite;
    private Integer idRegion;

    private String descripcion;
    private LocalDate fechaApertura;

    private Integer idJefaturaClienteSolicitante;
    private Integer idAnalistaClienteSolicitante;

    private Integer idCreador;                    // readonly
    private Integer idCoordinadorTiCw;
    private Integer idJefaturaResponsable;
    private Integer idLiquidador;
    private Integer idEjecutante;
    private Integer idAnalistaContable;

    private Boolean activo;
    private LocalDateTime fechaCreacion;
}