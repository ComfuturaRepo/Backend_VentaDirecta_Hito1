package com.backend.comfutura.dto.response.SSOMA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipanteDTO {
    private Integer idTrabajador;
    private String nombres;
    private String apellidos;
    private String cargo;
    private String rol;
}