package com.backend.comfutura.dto.Page.sitePage;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SiteRequestDTO {

    @NotBlank(message = "El código de sitio es obligatorio")
    @Size(min = 1, max = 50, message = "El código de sitio debe tener entre 1 y 50 caracteres")
    private String codigoSitio;

    private Boolean activo;
    private List<DescripcionDTO> descripciones;


}