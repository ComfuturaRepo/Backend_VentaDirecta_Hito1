package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.AnalistaClienteSolicitante;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnalistaClienteSolicitanteService {

    // Métodos existentes
    AnalistaClienteSolicitante guardar(AnalistaClienteSolicitante analista);
    List<AnalistaClienteSolicitante> listar();
    void toggle(Integer id);

    // Nuevos métodos para paginación
    PageResponseDTO<AnalistaClienteSolicitante> listar(Pageable pageable);
    PageResponseDTO<AnalistaClienteSolicitante> listarActivos(Pageable pageable);
    PageResponseDTO<AnalistaClienteSolicitante> buscar(String search, Pageable pageable);

    // Nuevo método para obtener por ID
    AnalistaClienteSolicitante obtenerPorId(Integer id);
}