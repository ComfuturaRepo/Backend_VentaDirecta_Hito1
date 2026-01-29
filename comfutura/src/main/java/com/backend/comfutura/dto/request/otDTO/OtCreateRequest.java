package com.backend.comfutura.dto.request.otDTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class OtCreateRequest {

    // Campos básicos
    private Integer idOtsAnterior;
    private Integer idOts;

    // Campos obligatorios para importación
    private Integer idCliente;
    private Integer idArea;
    private Integer idProyecto;
    private Integer idFase;
    private Integer idSite;
    private Integer idRegion;
    private Integer idEstadoOt; // <-- AGREGAR ESTE CAMPO
    private LocalDate fechaApertura;
    private Integer diasAsignados; // <-- AGREGAR ESTE CAMPO

    private String descripcion;

    // Campos opcionales
    private Integer idJefaturaClienteSolicitante;
    private Integer idAnalistaClienteSolicitante;
    private Integer idCoordinadorTiCw;
    private Integer idJefaturaResponsable;
    private Integer idLiquidador;
    private Integer idEjecutante;
    private Integer idAnalistaContable;

    // Campos de texto (para importación desde Excel)
    private String jefaturaClienteSolicitante; // <-- AGREGAR para importación
    private String analistaClienteSolicitante;  // <-- AGREGAR para importación

    private boolean activo;
}