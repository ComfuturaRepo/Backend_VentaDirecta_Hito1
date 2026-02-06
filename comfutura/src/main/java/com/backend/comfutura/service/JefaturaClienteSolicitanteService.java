package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.JefaturaClienteSolicitante;
import org.springframework.data.domain.Pageable;

public interface JefaturaClienteSolicitanteService {
    // Paginados
    PageResponseDTO<JefaturaClienteSolicitante> listar(Pageable pageable);
    PageResponseDTO<JefaturaClienteSolicitante> listarActivos(Pageable pageable);
    PageResponseDTO<JefaturaClienteSolicitante> buscar(String search, Pageable pageable);

    // CRUD
    JefaturaClienteSolicitante guardar(JefaturaClienteSolicitante jefatura);
    void toggle(Integer id);

    // Para mantener compatibilidad (opcional)
    java.util.List<JefaturaClienteSolicitante> listar();
}