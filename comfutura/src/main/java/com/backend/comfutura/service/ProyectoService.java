package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.response.ProyectoResponse;
import com.backend.comfutura.model.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProyectoService {
    // Paginados
    PageResponseDTO<ProyectoResponse> listarProyectos(Pageable pageable);
    PageResponseDTO<ProyectoResponse> listarTodos(Pageable pageable);
    PageResponseDTO<ProyectoResponse> buscarProyectos(String search, Pageable pageable);

    // CRUD
    ProyectoResponse crearProyecto(Proyecto proyecto);
    ProyectoResponse editarProyecto(Integer id, Proyecto proyectoActualizado);
    ProyectoResponse obtenerProyectoPorId(Integer id);
    ProyectoResponse toggleProyecto(Integer id);

    // Para mantener compatibilidad (opcional)
    Page<ProyectoResponse> listarProyectos(int page);
}