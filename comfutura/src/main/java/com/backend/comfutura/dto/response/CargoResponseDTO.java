package com.backend.comfutura.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CargoResponseDTO {
    private Integer id;
    private String nombre;
    private Integer idNivel;
    private String nombreNivel;
    private Boolean activo;
}