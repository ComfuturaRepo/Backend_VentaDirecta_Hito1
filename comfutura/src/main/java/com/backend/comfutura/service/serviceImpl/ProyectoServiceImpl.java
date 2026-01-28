package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.response.ProyectoResponse;
import com.backend.comfutura.model.Proyecto;
import com.backend.comfutura.repository.ProyectoRepository;
import com.backend.comfutura.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProyectoResponse> listarProyectos(Pageable pageable) {
        Page<Proyecto> page = proyectoRepository.findByActivoTrueOrderByNombreAsc(pageable);
        return toPageResponseDTO(page.map(this::toProyectoResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProyectoResponse> listarTodos(Pageable pageable) {
        Page<Proyecto> page = proyectoRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("nombre").ascending()
                )
        );
        return toPageResponseDTO(page.map(this::toProyectoResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProyectoResponse> buscarProyectos(String search, Pageable pageable) {
        Specification<Proyecto> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase().trim() + "%";
            return cb.like(cb.lower(root.get("nombre")), pattern);
        };

        Page<Proyecto> page = proyectoRepository.findAll(spec, pageable);
        return toPageResponseDTO(page.map(this::toProyectoResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProyectoResponse> listarProyectos(int page) {
        // Método para mantener compatibilidad con el controlador existente
        Pageable pageable = PageRequest.of(page, 20);
        Page<Proyecto> proyectos = proyectoRepository.findByActivoTrueOrderByNombreAsc(pageable);
        return proyectos.map(this::toProyectoResponse);
    }

    // Crear proyecto
    @Override
    @Transactional
    public ProyectoResponse crearProyecto(Proyecto proyecto) {
        proyecto.setActivo(true);
        Proyecto guardado = proyectoRepository.save(proyecto);
        return toProyectoResponse(guardado);
    }

    // Editar proyecto
    @Override
    @Transactional
    public ProyectoResponse editarProyecto(Integer id, Proyecto proyectoActualizado) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        proyecto.setNombre(proyectoActualizado.getNombre());
        Proyecto guardado = proyectoRepository.save(proyecto);
        return toProyectoResponse(guardado);
    }

    // Obtener un proyecto por ID
    @Override
    @Transactional(readOnly = true)
    public ProyectoResponse obtenerProyectoPorId(Integer id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));
        return toProyectoResponse(proyecto);
    }

    // Toggle: activar/desactivar proyecto
    @Override
    @Transactional
    public ProyectoResponse toggleProyecto(Integer id) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        proyecto.setActivo(!proyecto.getActivo());
        Proyecto guardado = proyectoRepository.save(proyecto);
        return toProyectoResponse(guardado);
    }

    private ProyectoResponse toProyectoResponse(Proyecto proyecto) {
        return ProyectoResponse.builder()
                .idProyecto(proyecto.getIdProyecto())
                .nombre(proyecto.getNombre())
                .activo(proyecto.getActivo())
                .build();
    }

    private <T> PageResponseDTO<T> toPageResponseDTO(Page<T> page) {
        return new PageResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.getSize()
        );
    }
}