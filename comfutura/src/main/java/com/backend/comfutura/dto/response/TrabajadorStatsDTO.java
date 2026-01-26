package com.backend.comfutura.dto.response;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TrabajadorStatsDTO {
    private long totalTrabajadores;
    private long trabajadoresActivos;
    private double porcentajeActivos;
    private Map<String, Long> porArea = new HashMap<>();
    private Map<String, Long> porCargo = new HashMap<>();
}