package com.backend.comfutura.dto.request.ssoma;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InspeccionTrabajadorDTO {
    private String tipoInspeccion;
    private Integer trabajadorId;
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
    private Integer responsableId;
}
