// OtDetailResponse.java  →  detalle completo (para vista de detalle)
package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class OtDetailResponse {
    private Integer idOts;
    private Integer ot;
    private Integer idOtsAnterior;

    private String descripcion;
    private LocalDate fechaApertura;
    private Integer diasAsignados;
    private LocalDateTime fechaCreacion;
    private Boolean activo;

    private Integer idCliente;
    private String clienteRazonSocial;

    private Integer idArea;
    private String areaNombre;

    private Integer idProyecto;
    private String proyectoNombre;

    private Integer idFase;
    private String faseNombre;

    private Integer idSite;
    private String siteNombre;

    private Integer idRegion;
    private String regionNombre;

    private Integer idJefaturaClienteSolicitante;
    private String jefaturaClienteSolicitanteNombre;

    private Integer idAnalistaClienteSolicitante;
    private String analistaClienteSolicitanteNombre;

    private Integer idCreador;
    private String creadorNombre;

    private Integer idCoordinadorTiCw;
    private String coordinadorTiCwNombre;

    private Integer idJefaturaResponsable;
    private String jefaturaResponsableNombre;

    private Integer idLiquidador;
    private String liquidadorNombre;

    private Integer idEjecutante;
    private String ejecutanteNombre;

    private Integer idAnalistaContable;
    private String analistaContableNombre;

    private String estadoOt;
}