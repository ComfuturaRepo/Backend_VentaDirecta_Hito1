package com.backend.comfutura.service;

import com.backend.comfutura.model.EstadoCompraDirecta;

import java.util.List;

public interface EstadoCompraDirectaService {

    // Para combos / selects
    List<EstadoCompraDirecta> listarActivos();

    // Para lógica de negocio (REGISTRADO, APROBADO, etc.)
    EstadoCompraDirecta buscarPorDescripcion(String descripcion);
}

