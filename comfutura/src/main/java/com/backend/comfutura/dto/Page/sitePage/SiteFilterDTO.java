package com.backend.comfutura.dto.Page.sitePage;

import lombok.Data;

@Data
public class SiteFilterDTO {
    private String search; // Buscar en código, descripción o dirección
    private Boolean activo; // null = todos, true = activos, false = inactivos
    private String codigoSitio; // Búsqueda exacta de código
    private String descripcion; // Búsqueda específica en descripción
    private String direccion; // Búsqueda específica en dirección
}