package com.backend.comfutura.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class SiteRequestDTO {
    private String codigoSitio;
    private Boolean activo;
    private List<SiteDescripcionRequestDTO> descripciones;
}

