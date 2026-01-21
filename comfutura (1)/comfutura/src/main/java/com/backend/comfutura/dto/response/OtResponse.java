package com.backend.comfutura.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtResponse {

    private Integer idOts;
    private Integer ot;                // número de OT

    private String descripcion;
    private LocalDate fechaApertura;
    private Integer diasAsignados;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    // Relaciones principales
    private String clienteRazonSocial;
    private String areaNombre;
    private String proyectoNombre;
    private String faseNombre;
    private String siteNombre;
    private String regionNombre;

    // Responsables (descripciones)
    private String jefaturaClienteSolicitante;
    private String analistaClienteSolicitante;
    private String coordinadorTiCw;
    private String jefaturaResponsable;
    private String liquidador;
    private String ejecutante;
    private String analistaContable;

    // Opcional: lista de trabajadores asignados con su rol
    private List<TrabajadorEnOtDto> trabajadoresAsignados;

    // DTO anidado para trabajadores
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrabajadorEnOtDto {
        private Integer idTrabajador;
        private String nombresCompletos;  // nombres + apellidos
        private String rolEnOt;
        private String cargoNombre;
        private String areaNombre;
    }
}