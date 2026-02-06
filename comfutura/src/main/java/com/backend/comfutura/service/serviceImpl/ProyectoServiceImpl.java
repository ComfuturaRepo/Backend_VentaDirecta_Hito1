package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.response.ProyectoResponse;
import com.backend.comfutura.model.Proyecto;
import com.backend.comfutura.repository.ProyectoRepository;
import com.backend.comfutura.service.ProyectoService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        Specification<Proyecto> spec = crearSpecificationBusqueda(search);

        Page<Proyecto> page = proyectoRepository.findAll(spec, pageable);
        return toPageResponseDTO(page.map(this::toProyectoResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProyectoResponse> listarProyectos(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Proyecto> proyectos = proyectoRepository.findByActivoTrueOrderByNombreAsc(pageable);
        return proyectos.map(this::toProyectoResponse);
    }

    // Crear proyecto con validación de duplicados
    @Override
    @Transactional
    public ProyectoResponse crearProyecto(Proyecto proyecto) {
        // Validar que no exista un proyecto con el mismo nombre
        validarNombreUnico(proyecto.getNombre(), null);

        proyecto.setActivo(true);
        Proyecto guardado = proyectoRepository.save(proyecto);
        return toProyectoResponse(guardado);
    }

    // Editar proyecto con validación de duplicados
    @Override
    @Transactional
    public ProyectoResponse editarProyecto(Integer id, Proyecto proyectoActualizado) {
        Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        // Validar que no exista otro proyecto con el mismo nombre (excepto este)
        validarNombreUnico(proyectoActualizado.getNombre(), id);

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

    // Nuevo método para búsqueda avanzada con múltiples filtros
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProyectoResponse> buscarProyectosAvanzado(
            String nombre,
            Boolean activo,
            Pageable pageable) {

        Specification<Proyecto> spec = crearSpecificationAvanzado(nombre, activo);
        Page<Proyecto> page = proyectoRepository.findAll(spec, pageable);
        return toPageResponseDTO(page.map(this::toProyectoResponse));
    }

    // Validar nombre único
    private void validarNombreUnico(String nombre, Integer idProyectoExcluir) {
        boolean nombreExiste;

        if (idProyectoExcluir != null) {
            // Para edición: verificar si existe otro proyecto con el mismo nombre
            nombreExiste = proyectoRepository.existsByNombreAndIdProyectoNot(nombre, idProyectoExcluir);
        } else {
            // Para creación: verificar si existe cualquier proyecto con el mismo nombre
            nombreExiste = proyectoRepository.existsByNombreIgnoreCase(nombre);
        }

        if (nombreExiste) {
            throw new RuntimeException("Ya existe un proyecto con el nombre: " + nombre);
        }
    }

    // Crear specification para búsqueda simple
    private Specification<Proyecto> crearSpecificationBusqueda(String search) {
        return (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase().trim() + "%";
            return cb.like(cb.lower(root.get("nombre")), pattern);
        };
    }

    // Crear specification para búsqueda avanzada
    private Specification<Proyecto> crearSpecificationAvanzado(String nombre, Boolean activo) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por nombre (búsqueda parcial case-insensitive)
            if (nombre != null && !nombre.trim().isEmpty()) {
                String pattern = "%" + nombre.toLowerCase().trim() + "%";
                predicates.add(cb.like(cb.lower(root.get("nombre")), pattern));
            }

            // Filtro por estado activo
            if (activo != null) {
                predicates.add(cb.equal(root.get("activo"), activo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
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