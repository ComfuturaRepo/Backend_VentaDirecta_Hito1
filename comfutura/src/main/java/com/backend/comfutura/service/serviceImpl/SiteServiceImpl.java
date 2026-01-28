package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.Site;
import com.backend.comfutura.repository.SiteRepository;
import com.backend.comfutura.service.SiteService;
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
public class SiteServiceImpl implements SiteService {

    private final SiteRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<Site> listarPaginado(Pageable pageable) {
        return repository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("codigoSitio").ascending()
                )
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<Site> listar(Pageable pageable) {
        Page<Site> page = repository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("codigoSitio").ascending()
                )
        );
        return toPageResponseDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<Site> listarActivos(Pageable pageable) {
        Page<Site> page = repository.findByActivoTrue(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("codigoSitio").ascending()
                )
        );
        return toPageResponseDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<Site> buscar(String search, Pageable pageable) {
        Specification<Site> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase().trim() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("codigoSitio")), pattern),
                    cb.like(cb.lower(root.get("descripcion")), pattern),
                    cb.like(cb.lower(root.get("direccion")), pattern)
            );
        };

        Page<Site> page = repository.findAll(spec, pageable);
        return toPageResponseDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Site obtenerPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public Site guardar(Site site) {
        // Validar código de sitio único
        if (site.getIdSite() == null) {
            if (existeCodigoSitio(site.getCodigoSitio())) {
                throw new RuntimeException("El código de sitio ya existe: " + site.getCodigoSitio());
            }
            site.setActivo(true);
        } else {
            if (existeCodigoSitioConOtroId(site.getCodigoSitio(), site.getIdSite())) {
                throw new RuntimeException("El código de sitio ya existe en otro registro: " + site.getCodigoSitio());
            }
        }

        return repository.save(site);
    }

    @Override
    @Transactional
    public void toggle(Integer id) {
        Site site = obtenerPorId(id);
        site.setActivo(!site.getActivo());
        repository.save(site);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCodigoSitio(String codigoSitio) {
        return repository.existsByCodigoSitio(codigoSitio);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCodigoSitioConOtroId(String codigoSitio, Integer id) {
        return repository.existsByCodigoSitioAndIdSiteNot(codigoSitio, id);
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