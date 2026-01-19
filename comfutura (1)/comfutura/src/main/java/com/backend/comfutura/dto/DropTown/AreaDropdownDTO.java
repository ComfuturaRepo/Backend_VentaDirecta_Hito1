package com.backend.comfutura.dto.DropTown;

import lombok.Data;

@Data
public class AreaDropdownDTO {
    private Integer idArea;
    private String nombre;

    public AreaDropdownDTO(Integer idArea, String nombre) {
        this.idArea = idArea;
        this.nombre = nombre;
    }
}