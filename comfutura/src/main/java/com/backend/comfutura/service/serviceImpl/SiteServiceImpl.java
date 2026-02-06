package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.SiteRequestDTO;
import com.backend.comfutura.dto.response.SiteDTO;
import com.backend.comfutura.dto.response.SiteDescripcionDTO;
import com.backend.comfutura.model.Site;
import com.backend.comfutura.model.SiteDescripcion;
import com.backend.comfutura.repository.SiteDescripcionRepository;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;
    private final SiteDescripcionRepository descripcionRepository;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<SiteDTO> listar(Pageable pageable) {
        Page<Site> page = siteRepository.findAll(
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
    public PageResponseDTO<SiteDTO> listarActivos(Pageable pageable) {
        Page<Site> page = siteRepository.findAllActivosWithDescripciones(
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
    public PageResponseDTO<SiteDTO> buscar(String search, Pageable pageable) {
        Specification<Site> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase().trim() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("codigoSitio")), pattern),
                    cb.like(cb.lower(root.join("descripciones").get("descripcion")), pattern),
                    cb.like(cb.lower(root.join("descripciones").get("direccion")), pattern)
            );
        };

        Page<Site> page = siteRepository.findAll(spec, pageable);
        return toPageResponseDTO(page);
    }

    @Override
    @Transactional(readOnly = true)
    public SiteDTO obtenerPorId(Integer id) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site no encontrado con ID: " + id));

        return toSiteDTO(site);
    }

    @Override
    @Transactional
    public SiteDTO guardar(SiteRequestDTO request) {
        // Validar código único
        if (existeCodigoSitio(request.getCodigoSitio())) {
            throw new RuntimeException("El código de sitio ya existe: " + request.getCodigoSitio());
        }

        // Crear site
        Site site = new Site();
        site.setCodigoSitio(request.getCodigoSitio());
        site.setActivo(request.getActivo() != null ? request.getActivo() : true);

        // Crear descripciones
        if (request.getDescripciones() != null) {
            List<SiteDescripcion> descripciones = request.getDescripciones().stream()
                    .map(dto -> {
                        SiteDescripcion desc = new SiteDescripcion();
                        desc.setDescripcion(dto.getDescripcion());
                        desc.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
                        desc.setSite(site);
                        return desc;
                    })
                    .collect(Collectors.toList());

            site.setDescripciones(descripciones);
        }

        Site saved = siteRepository.save(site);
        return toSiteDTO(saved);
    }

    @Override
    @Transactional
    public SiteDTO actualizar(Integer id, SiteRequestDTO request) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site no encontrado con ID: " + id));

        // Validar código único (excluyendo el actual)
        if (existeCodigoSitioConOtroId(request.getCodigoSitio(), id)) {
            throw new RuntimeException("El código de sitio ya existe en otro registro: " + request.getCodigoSitio());
        }

        // Actualizar datos básicos
        site.setCodigoSitio(request.getCodigoSitio());
        if (request.getActivo() != null) {
            site.setActivo(request.getActivo());
        }

        // Desactivar descripciones antiguas
        descripcionRepository.deactivateAllBySiteId(id);

        // Crear nuevas descripciones
        if (request.getDescripciones() != null) {
            List<SiteDescripcion> nuevasDescripciones = request.getDescripciones().stream()
                    .map(dto -> {
                        SiteDescripcion desc = new SiteDescripcion();
                        desc.setDescripcion(dto.getDescripcion());
                        desc.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
                        desc.setSite(site);
                        return desc;
                    })
                    .collect(Collectors.toList());

            site.getDescripciones().addAll(nuevasDescripciones);
        }

        Site updated = siteRepository.save(site);
        return toSiteDTO(updated);
    }

    @Override
    @Transactional
    public void toggle(Integer id) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site no encontrado con ID: " + id));
        site.setActivo(!site.getActivo());
        siteRepository.save(site);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCodigoSitio(String codigoSitio) {
        return siteRepository.existsByCodigoSitio(codigoSitio);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCodigoSitioConOtroId(String codigoSitio, Integer id) {
        return siteRepository.existsByCodigoSitioAndIdSiteNot(codigoSitio, id);
    }

    private PageResponseDTO<SiteDTO> toPageResponseDTO(Page<Site> page) {
        List<SiteDTO> content = page.getContent().stream()
                .map(this::toSiteDTO)
                .collect(Collectors.toList());

        return new PageResponseDTO<>(
                content,
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.getSize()
        );
    }

    private SiteDTO toSiteDTO(Site site) {
        SiteDTO dto = new SiteDTO();
        dto.setIdSite(site.getIdSite());
        dto.setCodigoSitio(site.getCodigoSitio());
        dto.setActivo(site.getActivo());
        dto.setFechaCreacion(site.getFechaCreacion());

        if (site.getDescripciones() != null) {
            List<SiteDescripcionDTO> descripcionesDTO = site.getDescripciones().stream()
                    .filter(SiteDescripcion::getActivo)
                    .map(desc -> {
                        SiteDescripcionDTO descDTO = new SiteDescripcionDTO();
                        descDTO.setIdSiteDescripcion(desc.getIdSiteDescripcion());
                        descDTO.setDescripcion(desc.getDescripcion());
                        descDTO.setActivo(desc.getActivo());
                        descDTO.setFechaCreacion(desc.getFechaCreacion());
                        return descDTO;
                    })
                    .collect(Collectors.toList());

            dto.setDescripciones(descripcionesDTO);
        }

        return dto;
    }
}