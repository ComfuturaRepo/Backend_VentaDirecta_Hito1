package com.backend.comfutura.dto.response.SSOMA;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SSTFormResponseDTO {
    private Integer idATS;
    private Integer idCapacitacion;
    private Integer idInspeccionEPP;
    private Integer idInspeccionHerramienta;
    private Integer idPETAR;

    private String numeroRegistroATS;
    private String numeroRegistroCapacitacion;
    private String numeroRegistroInspeccionEPP;
    private String numeroRegistroInspeccionHerramienta;
    private String numeroRegistroPETAR;

    private LocalDate fecha;
    private LocalTime hora;
    private String empresa;
    private String lugarTrabajo;

    private String mensaje;
}