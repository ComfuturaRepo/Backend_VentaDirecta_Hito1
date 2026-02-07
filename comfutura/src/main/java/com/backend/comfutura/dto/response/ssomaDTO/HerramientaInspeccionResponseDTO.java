package com.backend.comfutura.dto.response.ssomaDTO;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HerramientaInspeccionResponseDTO {
    private String herramientaNombre;
    private Boolean p1;
    private Boolean p2;
    private Boolean p3;
    private Boolean p4;
    private Boolean p5;
    private Boolean p6;
    private Boolean p7;
    private Boolean p8;
    private String observaciones;
    private List<String> fotoUrls;
}