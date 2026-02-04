package com.backend.comfutura.dto.request.SSOMA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PETARRequestDTO {
    private List<PETARRespuestaDTO> respuestas;
    private List<Integer> trabajadoresAutorizadosIds;
}

