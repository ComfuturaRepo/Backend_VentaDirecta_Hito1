package com.backend.comfutura.dto.request.SSOMA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTOs auxiliares
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATSParticipanteDTO {
    private Integer idTrabajador;
    private Integer idRol;
}
