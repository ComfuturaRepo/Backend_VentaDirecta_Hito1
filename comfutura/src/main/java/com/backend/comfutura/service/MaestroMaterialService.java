package com.backend.comfutura.service;

import com.backend.comfutura.dto.response.MaestroMaterialResponse;
import org.springframework.data.domain.Page;

public interface MaestroMaterialService {

    // Listado paginado
    Page<MaestroMaterialResponse> listar(int page);

    // Crear
    MaestroMaterialResponse crear(MaestroMaterialResponse request);

    // Editar
    MaestroMaterialResponse editar(Integer id, MaestroMaterialResponse request);

    // Obtener por ID
    MaestroMaterialResponse obtenerPorId(Integer id);

    // Toggle (activar / desactivar)
    void cambiarEstado(Integer id);
}