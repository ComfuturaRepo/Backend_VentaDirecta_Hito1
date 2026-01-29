package com.backend.comfutura.dto.response.clienteDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteSimpleDTO {
    private Integer idCliente;
    private String razonSocial;
    private String ruc;
    private Boolean activo;
}