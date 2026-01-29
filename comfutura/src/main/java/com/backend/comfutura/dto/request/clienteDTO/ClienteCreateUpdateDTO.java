package com.backend.comfutura.dto.request.clienteDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteCreateUpdateDTO {
    private String razonSocial;
    private String ruc;
    private List<Integer> areaIds; // IDs de las áreas a asociar
}