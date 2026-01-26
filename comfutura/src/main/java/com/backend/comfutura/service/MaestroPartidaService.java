package com.backend.comfutura.service;

import com.backend.comfutura.dto.response.MaestroPartidaResponse;
import org.springframework.data.domain.Page;

public interface MaestroPartidaService {

    // Listado paginado (como Proyecto)
    Page<MaestroPartidaResponse> listar(int page);

    // Crear
    MaestroPartidaResponse crear(MaestroPartidaResponse request);

    // Editar
    MaestroPartidaResponse editar(Integer id, MaestroPartidaResponse request);

    // Obtener por ID
    MaestroPartidaResponse obtenerPorId(Integer id);

    // Toggle (activar / desactivar)
    void cambiarEstado(Integer id);
}