package com.backend.comfutura.dto.response.ssomaDTO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipanteResponseDTO {
    private Integer idParticipante;
    private String nombre;
    private String cargo;
    private String firmaUrl;
    private List<String> fotoUrls;
}