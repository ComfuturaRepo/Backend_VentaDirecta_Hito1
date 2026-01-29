package com.backend.comfutura.dto.response.clienteDTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteDetailDTO {
    private Integer idCliente;
    private String razonSocial;
    private String ruc;
    private Boolean activo;
    private List<AreaSimpleDTO> areas = new ArrayList<>();
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
}