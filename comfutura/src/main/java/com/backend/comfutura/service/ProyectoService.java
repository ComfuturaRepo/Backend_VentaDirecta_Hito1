package com.backend.comfutura.service;

import com.backend.comfutura.dto.response.ProyectoResponse;
import com.backend.comfutura.model.Proyecto;
import org.springframework.data.domain.Page;

public interface ProyectoService {

    // Listar con paginación
    Page<ProyectoResponse> listarProyectos(int page);

    // Crear
    ProyectoResponse crearProyecto(Proyecto proyecto);

    // Editar
    ProyectoResponse editarProyecto(Integer id, Proyecto proyectoActualizado);

    // Obtener por ID
    ProyectoResponse obtenerProyectoPorId(Integer id);

    // Toggle activar/desactivar
    ProyectoResponse toggleProyecto(Integer id);
}