package com.backend.comfutura.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// Para listado y detalle rápido (lo que más se usa)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtResponse {

    private Integer idOts;
    private Integer ot;

    private String descripcion;
    private LocalDate fechaApertura;
    private Integer diasAsignados;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    // Entidades principales
    private String clienteRazonSocial;
    private String areaNombre;
    private String proyectoNombre;
    private String faseNombre;
    private String siteCodigo;          // o siteNombre si prefieres
    private String regionNombre;

    // Solicitante (cliente)
    private String jefaturaClienteSolicitante;
    private String analistaClienteSolicitante;

    // Responsables internos (nombres completos)
    private String creadorNombre;           // ← el id_trabajador original
    private String coordinadorTiCw;
    private String jefaturaResponsable;
    private String liquidador;
    private String ejecutante;
    private String analistaContable;
    private String estadoOt;
}