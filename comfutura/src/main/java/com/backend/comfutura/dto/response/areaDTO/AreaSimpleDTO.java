package com.backend.comfutura.dto.response.areaDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaSimpleDTO {
    private Integer idArea;
    private String nombre;
    private Boolean activo;
}