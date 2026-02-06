package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.JefaturaClienteSolicitante;
import com.backend.comfutura.repository.JefaturaClienteSolicitanteRepository;
import com.backend.comfutura.service.JefaturaClienteSolicitanteService;
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
public class JefaturaClienteSolicitanteServiceImpl implements JefaturaClienteSolicitanteService {

    private final JefaturaClienteSolicitanteRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<JefaturaClienteSolicitante> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<JefaturaClienteSolicitante> listar(Pageable pageable) {
        Page<JefaturaClienteSolicitante> page = repository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("descripcion").ascending()
                )
        );
        return toPageResponseDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<JefaturaClienteSolicitante> listarActivos(Pageable pageable) {
        Page<JefaturaClienteSolicitante> page = repository.findByActivoTrue(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("descripcion").ascending()
                )
        );
        return toPageResponseDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<JefaturaClienteSolicitante> buscar(String search, Pageable pageable) {
        Specification<JefaturaClienteSolicitante> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase().trim() + "%";
            return cb.like(cb.lower(root.get("descripcion")), pattern);
        };

        Page<JefaturaClienteSolicitante> page = repository.findAll(spec, pageable);
        return toPageResponseDTO(page);
    }

    @Override
    @Transactional
    public JefaturaClienteSolicitante guardar(JefaturaClienteSolicitante jefatura) {
        if (jefatura.getId() == null) {
            jefatura.setActivo(true);
            return repository.save(jefatura);
        }

        JefaturaClienteSolicitante db = repository.findById(jefatura.getId())
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        db.setDescripcion(jefatura.getDescripcion());
        return repository.save(db);
    }

    @Override
    @Transactional
    public void toggle(Integer id) {
        JefaturaClienteSolicitante jefatura = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        jefatura.setActivo(!jefatura.getActivo());
        repository.save(jefatura);
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