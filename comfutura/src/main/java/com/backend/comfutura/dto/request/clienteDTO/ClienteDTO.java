package com.backend.comfutura.dto.request.clienteDTO;

import com.backend.comfutura.dto.response.clienteDTO.AreaSimpleDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteDTO {
    private Integer idCliente;
    private String razonSocial;
    private String ruc;
    private Boolean activo = true;
    private List<AreaSimpleDTO> areas = new ArrayList<>();
}