package com.backend.comfutura.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SiteDTO {
    private Integer idSite;
    private String codigoSitio;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    private List<SiteDescripcionDTO> descripciones;
}
