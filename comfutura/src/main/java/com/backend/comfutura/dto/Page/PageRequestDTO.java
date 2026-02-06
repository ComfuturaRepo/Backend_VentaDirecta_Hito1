package com.backend.comfutura.dto.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestDTO {
    private int page = 0;
    private int size = 10;
    private String sortBy = "idCliente";
    private String sortDir = "asc";
}