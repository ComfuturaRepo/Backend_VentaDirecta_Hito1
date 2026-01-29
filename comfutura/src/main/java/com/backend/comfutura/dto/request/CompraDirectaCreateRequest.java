package com.backend.comfutura.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CompraDirectaCreateRequest {

    private Integer idOts;
    private LocalDate fechaCosto;
    private String observacion;
}
