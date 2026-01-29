package com.backend.comfutura.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CambioEstadoCompraDirectaRequest {

    // Ejemplos:
    // REGISTRADO
    // POR APROBACION PRESUPUESTO
    // PRES. APROBADO
    // RECHAZADO
    // ANULADO
    private String estado;

    // Observación opcional
    private String observacion;
}


