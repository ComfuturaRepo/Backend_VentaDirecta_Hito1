package com.backend.comfutura.dto.request.ssomaDTO;


import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PetarRequest {
    private String numeroRegistro;
    private LocalDate fecha;
    private List<PetarRespuestaRequest> respuestas;
    private List<Integer> trabajadoresAutorizadosIds;
}
