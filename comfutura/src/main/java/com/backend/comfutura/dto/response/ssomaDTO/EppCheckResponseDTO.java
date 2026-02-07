package com.backend.comfutura.dto.response.ssomaDTO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EppCheckResponseDTO {
    private String eppNombre;
    private Boolean usado;
    private List<String> fotoUrls;
}