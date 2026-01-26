package com.backend.comfutura.service;

import com.backend.comfutura.dto.response.MaestroServicioResponse;
import org.springframework.data.domain.Page;

public interface MaestroServicioService {

    // Listado paginado
    Page<MaestroServicioResponse> listar(int page);

    // Crear
    MaestroServicioResponse crear(MaestroServicioResponse request);

    // Editar
    MaestroServicioResponse editar(Integer id, MaestroServicioResponse request);

    // Obtener por ID
    MaestroServicioResponse obtenerPorId(Integer id);

    // Toggle (activar / desactivar)
    void cambiarEstado(Integer id);
}