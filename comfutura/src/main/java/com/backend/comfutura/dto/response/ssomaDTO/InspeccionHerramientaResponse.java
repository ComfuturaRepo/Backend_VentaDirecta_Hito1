package com.backend.comfutura.dto.response.ssomaDTO;

import com.backend.comfutura.dto.request.ssomaDTO.InspeccionHerramientaDetalleRequest;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class InspeccionHerramientaResponse {
    private Integer id;
    private String numeroRegistro;
    private LocalDate fecha;
    private Integer idSupervisor;
    private String nombreSupervisor;
    private List<InspeccionHerramientaDetalleRequest> detalles;
}