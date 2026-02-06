package com.backend.comfutura.dto.request.ssomaDTO;

import lombok.Data;

@Data
public class SsomaCompletoRequest {
    private AtsRequest ats;
    private CapacitacionRequest capacitacion;
    private InspeccionEppRequest inspeccionEpp;
    private InspeccionHerramientaRequest inspeccionHerramienta;
    private PetarRequest petar;
}
