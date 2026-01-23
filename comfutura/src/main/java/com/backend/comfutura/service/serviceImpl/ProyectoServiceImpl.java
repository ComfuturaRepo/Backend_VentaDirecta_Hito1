package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.response.ProyectoResponse;
import com.backend.comfutura.model.Proyecto;
import com.backend.comfutura.repository.ProyectoRepository;
import com.backend.comfutura.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;

    // Listar proyectos con paginación
    @Override
    public Page<ProyectoResponse> listarProyectos(int page) {
        PageRequest pageable = PageRequest.of(page, 20);

        Page<Proyecto> proyectos = proyectoRepository.findByActivoTrueOrderByNombreAsc(pageable);

        return proyectos.map(proyecto -> ProyectoResponse.builder()
                .idProyecto(proyecto.getIdProyecto())
                .nombre(proyecto.getNombre())
                .activo(proyecto.getActivo())
                .build());
    }

    // Crear proyecto
    @Override
    public ProyectoResponse crearProyecto(Proyecto proyecto) {
        proyecto.setActivo(true); // siempre activo al crear
        Proyecto guardado = proyectoRepository.save(proyecto);

        return ProyectoResponse.builder()
                .idProyecto(guardado.getIdProyecto())
                .nombre(guardado.getNombre())
                .activo(guardado.getActivo())
                .build();
    }

    // Editar proyecto
    @Override
    public ProyectoResponse editarProyecto(Integer id, Proyecto proyectoActualizado) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        proyecto.setNombre(proyectoActualizado.getNombre());
        // No cambiamos el estado activo aquí

        Proyecto guardado = proyectoRepository.save(proyecto);

        return ProyectoResponse.builder()
                .idProyecto(guardado.getIdProyecto())
                .nombre(guardado.getNombre())
                .activo(guardado.getActivo())
                .build();
    }

    // Obtener un proyecto por ID
    @Override
    public ProyectoResponse obtenerProyectoPorId(Integer id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        return ProyectoResponse.builder()
                .idProyecto(proyecto.getIdProyecto())
                .nombre(proyecto.getNombre())
                .activo(proyecto.getActivo())
                .build();
    }

    // Toggle: activar/desactivar proyecto
    @Override
    public ProyectoResponse toggleProyecto(Integer id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        proyecto.setActivo(!proyecto.getActivo());
        Proyecto guardado = proyectoRepository.save(proyecto);

        return ProyectoResponse.builder()
                .idProyecto(guardado.getIdProyecto())
                .nombre(guardado.getNombre())
                .activo(guardado.getActivo())
                .build();
    }
}