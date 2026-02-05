package com.backend.comfutura.dto.response.ssomaDTO;

import com.backend.comfutura.dto.request.ssomaDTO.InspeccionEppDetalleRequest;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class InspeccionEppResponse {
    private Integer id;
    private String numeroRegistro;
    private LocalDate fecha;
    private Integer idInspector;
    private String nombreInspector;
    private List<InspeccionEppDetalleRequest> detalles;
}