package com.backend.comfutura.dto.response.ssomaDTO;

import com.backend.comfutura.dto.request.ssomaDTO.PetarRespuestaRequest;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class PetarResponse {
    private Integer id;
    private String numeroRegistro;
    private LocalDate fecha;
    private List<PetarRespuestaRequest> respuestas;
    private List<Integer> trabajadoresAutorizadosIds;
}