package com.backend.comfutura.dto.response.ssomaDTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspeccionTrabajadorResponseDTO {
    private String tipoInspeccion;
    private String trabajadorNombre;
    private Boolean casco;
    private Boolean lentes;
    private Boolean orejeras;
    private Boolean tapones;
    private Boolean guantes;
    private Boolean botas;
    private Boolean arnes;
    private Boolean chaleco;
    private Boolean mascarilla;
    private Boolean gafas;
    private String otros;
    private String accionCorrectiva;
    private String seguimiento;
    private String responsableNombre;
}