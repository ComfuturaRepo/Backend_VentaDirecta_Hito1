package com.backend.comfutura.dto.response.SSOMA;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATSListDTO {
    private Integer idATS;
    private LocalDate fecha;
    private LocalTime hora;
    private String empresa;
    private String lugarTrabajo;
    private String trabajo;
    private Integer cantidadParticipantes;
}