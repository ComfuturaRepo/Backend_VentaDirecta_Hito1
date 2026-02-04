package com.backend.comfutura.dto.request.SSOMA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspeccionHerramientaRequestDTO {
    private Integer idSupervisor;
    private List<InspeccionHerramientaDetalleDTO> detalles;
}
