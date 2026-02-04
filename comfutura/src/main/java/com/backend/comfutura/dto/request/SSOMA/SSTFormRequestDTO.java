package com.backend.comfutura.dto.request.SSOMA;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SSTFormRequestDTO {
    // Datos generales
    private String empresa;
    private String lugarTrabajo;
    private LocalDate fecha;
    private LocalTime hora;

    // HOJA 1: ATS
    private ATSRequestDTO ats;

    // HOJA 2: Capacitación
    private CapacitacionRequestDTO capacitacion;

    // HOJA 3: Inspección EPP
    private InspeccionEPPRequestDTO inspeccionEPP;

    // HOJA 4: Inspección Herramientas
    private InspeccionHerramientaRequestDTO inspeccionHerramienta;

    // HOJA 5: PETAR
    private PETARRequestDTO petar;
}





