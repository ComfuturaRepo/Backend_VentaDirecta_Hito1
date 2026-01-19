package com.backend.comfutura.dto.DropTown;

import lombok.Data;

@Data
public class ClienteDropdownDTO {
    private Integer idCliente;
    private String razonSocial;
    private String ruc;

    public ClienteDropdownDTO(Integer idCliente, String razonSocial, String ruc) {
        this.idCliente = idCliente;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
    }
}