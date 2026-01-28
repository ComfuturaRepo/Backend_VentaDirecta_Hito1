package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.AnalistaClienteSolicitante;
import com.backend.comfutura.repository.AnalistaClienteSolicitanteRepository;
import com.backend.comfutura.service.AnalistaClienteSolicitanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalistaClienteSolicitanteServiceImpl
        implements AnalistaClienteSolicitanteService {

    private final AnalistaClienteSolicitanteRepository repository;

    // CREAR + EDITAR
    @Override
    @Transactional
    public AnalistaClienteSolicitante guardar(AnalistaClienteSolicitante analista) {

        if (analista.getId() == null) {
            // CREAR
            analista.setActivo(true);
        } else {
            // EDITAR
            AnalistaClienteSolicitante db = repository.findById(analista.getId())
                    .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

            db.setDescripcion(analista.getDescripcion());
            return repository.save(db);
        }

        return repository.save(analista);
    }

    // LISTAR COMPLETO (sin paginación - para compatibilidad)
    @Override
    @Transactional(readOnly = true)
    public List<AnalistaClienteSolicitante> listar() {
        return repository.findByActivoTrueOrderByDescripcionAsc();
    }

    // LISTAR CON PAGINACIÓN
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<AnalistaClienteSolicitante> listar(Pageable pageable) {
        Page<AnalistaClienteSolicitante> page = repository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("descripcion").ascending()
                )
        );
        return toPageResponseDTO(page);
    }

    // LISTAR ACTIVOS CON PAGINACIÓN
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<AnalistaClienteSolicitante> listarActivos(Pageable pageable) {
        Page<AnalistaClienteSolicitante> page = repository.findByActivoTrue(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("descripcion").ascending()
                )
        );
        return toPageResponseDTO(page);
    }

    // BUSCAR CON PAGINACIÓN
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<AnalistaClienteSolicitante> buscar(String search, Pageable pageable) {
        Specification<AnalistaClienteSolicitante> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase().trim() + "%";
            return cb.like(cb.lower(root.get("descripcion")), pattern);
        };

        Page<AnalistaClienteSolicitante> page = repository.findAll(spec, pageable);
        return toPageResponseDTO(page);
    }

    // TOGGLE ACTIVO/INACTIVO
    @Override
    @Transactional
    public void toggle(Integer id) {

        AnalistaClienteSolicitante analista = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        analista.setActivo(!analista.getActivo());
        repository.save(analista);
    }

    // OBTENER POR ID
    @Override
    @Transactional(readOnly = true)
    public AnalistaClienteSolicitante obtenerPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Analista no encontrado con ID: " + id));
    }

    // MÉTODO AUXILIAR PARA CONVERTIR PAGE A PageResponseDTO
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