package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.model.Site;
import com.backend.comfutura.repository.SiteRepository;
import com.backend.comfutura.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {

    private final SiteRepository repository;

    // CREAR + EDITAR
    @Override
    @Transactional
    public Site guardar(Site site) {

        if (site.getIdSite() == null) {
            site.setActivo(true);
            return repository.save(site);
        }

        Site db = repository.findById(site.getIdSite())
                .orElseThrow(() -> new RuntimeException("Site no encontrado"));

        db.setCodigoSitio(site.getCodigoSitio());
        db.setDescripcion(site.getDescripcion());

        return repository.save(db);
    }

    // LISTAR CON PAGINACIÓN (TODOS)
    @Override
    @Transactional(readOnly = true)
    public Page<Site> listar(Pageable pageable) {
        Pageable safePageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("codigoSitio").ascending()
        );
        return repository.findAll(safePageable);
    }


    // TOGGLE ACTIVO / INACTIVO
    @Override
    @Transactional
    public void toggle(Integer id) {

        Site site = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site no encontrado"));

        site.setActivo(!site.getActivo());
        repository.save(site);
    }
}
