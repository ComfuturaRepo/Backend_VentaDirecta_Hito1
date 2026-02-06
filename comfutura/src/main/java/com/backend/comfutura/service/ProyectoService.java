package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.response.ProyectoResponse;
import com.backend.comfutura.model.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProyectoService {

    PageResponseDTO<ProyectoResponse> listarProyectos(Pageable pageable);

    PageResponseDTO<ProyectoResponse> listarTodos(Pageable pageable);

    PageResponseDTO<ProyectoResponse> buscarProyectos(String search, Pageable pageable);

    // Nuevo método para búsqueda avanzada
    PageResponseDTO<ProyectoResponse> buscarProyectosAvanzado(String nombre, Boolean activo, Pageable pageable);

    Page<ProyectoResponse> listarProyectos(int page);

    ProyectoResponse crearProyecto(Proyecto proyecto);

    ProyectoResponse editarProyecto(Integer id, Proyecto proyectoActualizado);

    ProyectoResponse obtenerProyectoPorId(Integer id);

    ProyectoResponse toggleProyecto(Integer id);
}