package com.backend.comfutura.dto.response.ssomaDTO;


import lombok.Data;

@Data
public class SsomaResponse {
    private String mensaje;
    private String transaccionId;
    private AtsResponse ats;
    private CapacitacionResponse capacitacion;
    private InspeccionEppResponse inspeccionEpp;
    private InspeccionHerramientaResponse inspeccionHerramienta;
    private PetarResponse petar;
}