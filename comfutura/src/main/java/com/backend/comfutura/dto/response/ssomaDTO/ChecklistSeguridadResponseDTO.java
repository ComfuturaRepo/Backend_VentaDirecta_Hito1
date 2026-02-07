package com.backend.comfutura.dto.response.ssomaDTO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChecklistSeguridadResponseDTO {
    private String itemNombre;
    private Boolean usado;
    private String observaciones;
    private List<String> fotoUrls;
}