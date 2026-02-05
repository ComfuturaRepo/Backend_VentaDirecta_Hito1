package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class InspeccionEppRequest {
    private String numeroRegistro;
    private LocalDate fecha;
    private Integer idInspector;
    private List<InspeccionEppDetalleRequest> detalles;
}

