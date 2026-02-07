package com.backend.comfutura.dto.response.ssomaDTO;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipoProteccionResponseDTO {
    private String equipoNombre;
    private Boolean usado;
}