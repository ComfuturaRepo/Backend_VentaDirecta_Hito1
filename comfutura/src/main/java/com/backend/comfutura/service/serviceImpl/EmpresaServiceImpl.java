package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.Empresa;
import com.backend.comfutura.repository.EmpresaRepository;
import com.backend.comfutura.service.EmpresaService;
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
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empresa> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<Empresa> listar(Pageable pageable) {
        Page<Empresa> page = repository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("razonSocial").ascending()
                )
        );
        return toPageResponseDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<Empresa> listarActivos(Pageable pageable) {
        Page<Empresa> page = repository.findByActivoTrue(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("razonSocial").ascending()
                )
        );
        return toPageResponseDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<Empresa> buscar(String search, Pageable pageable) {
        Specification<Empresa> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase().trim() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("razonSocial")), pattern),
                    cb.like(cb.lower(root.get("ruc")), pattern),
                    cb.like(cb.lower(root.get("nombreComercial")), pattern)
            );
        };

        Page<Empresa> page = repository.findAll(spec, pageable);
        return toPageResponseDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Empresa obtenerPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
    }

    @Override
    @Transactional
    public Empresa guardar(Empresa empresa) {
        if (empresa.getId() == null) {
            empresa.setActivo(true);
        }
        return repository.save(empresa);
    }

    @Override
    @Transactional
    public Empresa toggleActivo(Integer id) {
        Empresa empresa = obtenerPorId(id);
        empresa.setActivo(!empresa.getActivo());
        return repository.save(empresa);
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