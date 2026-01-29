package com.backend.comfutura.dto.request.areaDTO;

import com.backend.comfutura.dto.response.clienteDTO.ClienteSimpleDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AreaDTO {
    private Integer idArea;
    private String nombre;
    private Boolean activo = true;
    private List<ClienteSimpleDTO> clientes;
}