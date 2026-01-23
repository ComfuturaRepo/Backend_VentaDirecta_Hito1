package com.backend.comfutura.dto.request;



import lombok.Data;

@Data
public class CargoRequestDTO {
    private Integer id;
    private String nombre;
    private Integer idNivel;
}