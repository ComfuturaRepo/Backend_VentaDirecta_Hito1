package com.backend.comfutura.dto.request;


import lombok.Data;
import java.util.List;

@Data
public class TrabajadorFilterDTO {
    private String search;
    private Boolean activo;
    private List<Integer> areaIds;        // ← debe ser List, no Integer
    private List<Integer> cargoIds;       // ← debe ser List, no Integer
    private List<Integer> empresaIds;     // ← debe ser List, no Integer
    private List<Boolean> puedeSerLiquidador;
    private List<Boolean> puedeSerEjecutante;
    private List<Boolean> puedeSerAnalistaContable;
    private List<Boolean> puedeSerJefaturaResponsable;
    private List<Boolean> puedeSerCoordinadorTiCw;

    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "fechaCreacion";
    private String sortDirection = "DESC";
}