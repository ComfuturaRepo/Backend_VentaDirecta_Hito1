package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OtFullDetailResponse {

    private Integer idOts;
    private Integer ot;                    // número legible de la OT
    private Integer idOtsAnterior;

    private String descripcion;
    private LocalDate fechaApertura;
    private LocalDateTime fechaCreacion;
    private Boolean activo;

    // ── Entidades relacionadas ────────────────
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

    // ── Responsables ──────────────────────────
    private Integer idJefaturaClienteSolicitante;
    private String  jefaturaClienteSolicitanteNombre;

    private Integer idAnalistaClienteSolicitante;
    private String  analistaClienteSolicitanteNombre;

    private Integer idCoordinadorTiCw;
    private String  coordinadorTiCwNombre;

    private Integer idJefaturaResponsable;
    private String  jefaturaResponsableNombre;

    private Integer idLiquidador;
    private String  liquidadorNombre;

    private Integer idEjecutante;
    private String  ejecutanteNombre;

    private Integer idAnalistaContable;
    private String  analistaContableNombre;

    // ── Trabajadores asignados ────────────────
    private List<TrabajadorEnOtDto> trabajadoresAsignados;

    @Data
    @Builder
    public static class TrabajadorEnOtDto {
        private Integer idTrabajador;
        private String nombresCompletos;
        private String cargoNombre;
        private String areaTrabajadorNombre;
        private String rolEnOt;
        private Boolean activo;
    }
}