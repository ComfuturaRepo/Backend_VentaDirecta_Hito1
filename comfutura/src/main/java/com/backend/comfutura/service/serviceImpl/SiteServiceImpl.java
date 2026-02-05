package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.Page.sitePage.DescripcionDTO;
import com.backend.comfutura.dto.Page.sitePage.SiteFilterDTO;
import com.backend.comfutura.dto.request.SiteDescripcionRequestDTO;
import com.backend.comfutura.dto.request.SiteRequestDTO;
import com.backend.comfutura.dto.response.SiteDTO;
import com.backend.comfutura.dto.response.SiteDescripcionDTO;
import com.backend.comfutura.model.Site;
import com.backend.comfutura.model.SiteDescripcion;
import com.backend.comfutura.repository.SiteDescripcionRepository;
import com.backend.comfutura.repository.SiteRepository;
import com.backend.comfutura.service.SiteService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        SiteFilterDTO filtros = new SiteFilterDTO();
        filtros.setSearch(search);
        return listarConFiltros(filtros, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<SiteDTO> listarConFiltros(SiteFilterDTO filtros, Pageable pageable) {
        Specification<Site> spec = buildSpecification(filtros);

        Page<Site> page = siteRepository.findAll(spec, pageable);
        Map<String, Object> appliedFilters = buildFiltersMap(filtros);

        return toPageResponseDTO(page, appliedFilters);
    }

    @Override
    @Transactional(readOnly = true)
    public SiteDTO obtenerPorId(Integer id) {
        Site site = siteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Site no encontrado con ID: " + id));

        // Filtrar descripciones activas manualmente
        List<SiteDescripcion> descripcionesActivas = site.getDescripciones().stream()
                .filter(SiteDescripcion::getActivo)
                .collect(Collectors.toList());
        site.setDescripciones(descripcionesActivas);

        return toSiteDTO(site);
    }

    @Override
    @Transactional
    public SiteDTO guardar(SiteRequestDTO request) {
        // Validar código único
        validarCodigoSitio(request.getCodigoSitio(), null);

        // Validar descripciones
        validarDescripciones(request.getDescripciones());

        // Crear site
        Site site = new Site();
        site.setCodigoSitio(request.getCodigoSitio().trim());
        site.setActivo(request.getActivo() != null ? request.getActivo() : true);

        // Crear descripciones
        if (request.getDescripciones() != null && !request.getDescripciones().isEmpty()) {
            List<SiteDescripcion> descripciones = request.getDescripciones().stream()
                    .map(dto -> {
                        SiteDescripcion desc = SiteDescripcion.builder()
                                .descripcion(dto.getDescripcion().trim())
                                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                                .site(site)
                                .build();
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
        validarCodigoSitio(request.getCodigoSitio(), id);

        // Validar descripciones
        validarDescripciones(request.getDescripciones());

        // Actualizar datos básicos
        site.setCodigoSitio(request.getCodigoSitio().trim());
        if (request.getActivo() != null) {
            site.setActivo(request.getActivo());
        }

        // Desactivar descripciones antiguas
        if (site.getDescripciones() != null) {
            site.getDescripciones().forEach(desc -> desc.setActivo(false));
        }

        // Crear nuevas descripciones
        if (request.getDescripciones() != null && !request.getDescripciones().isEmpty()) {
            List<SiteDescripcion> nuevasDescripciones = request.getDescripciones().stream()
                    .map(dto -> {
                        SiteDescripcion desc = SiteDescripcion.builder()
                                .descripcion(dto.getDescripcion().trim())
                                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                                .site(site)
                                .build();
                        return desc;
                    })
                    .collect(Collectors.toList());

            // Agregar las nuevas descripciones
            if (site.getDescripciones() == null) {
                site.setDescripciones(new ArrayList<>());
            }
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
        return siteRepository.existsByCodigoSitio(codigoSitio.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeCodigoSitioConOtroId(String codigoSitio, Integer id) {
        return siteRepository.existsByCodigoSitioAndIdSiteNot(codigoSitio.trim(), id);
    }

    private Specification<Site> buildSpecification(SiteFilterDTO filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por estado activo/inactivo
            if (filtros.getActivo() != null) {
                predicates.add(cb.equal(root.get("activo"), filtros.getActivo()));
            }

            // Filtro por código exacto
            if (StringUtils.hasText(filtros.getCodigoSitio())) {
                predicates.add(cb.equal(
                        cb.lower(root.get("codigoSitio")),
                        filtros.getCodigoSitio().toLowerCase().trim()
                ));
            }

            // Búsqueda general (search)
            if (StringUtils.hasText(filtros.getSearch())) {
                String pattern = "%" + filtros.getSearch().toLowerCase().trim() + "%";

                Predicate searchPredicate = cb.or(
                        cb.like(cb.lower(root.get("codigoSitio")), pattern),
                        cb.like(cb.lower(root.join("descripciones").get("descripcion")), pattern)
                );

                predicates.add(searchPredicate);
            }

            // Filtro específico por descripción
            if (StringUtils.hasText(filtros.getDescripcion())) {
                String pattern = "%" + filtros.getDescripcion().toLowerCase().trim() + "%";
                predicates.add(cb.like(cb.lower(root.join("descripciones").get("descripcion")), pattern));
            }

            // Solo descripciones activas en el join
            Predicate descripcionActiva = cb.equal(root.join("descripciones").get("activo"), true);
            predicates.add(descripcionActiva);

            query.distinct(true);

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Map<String, Object> buildFiltersMap(SiteFilterDTO filtros) {
        Map<String, Object> filtersMap = new HashMap<>();

        if (filtros.getSearch() != null) {
            filtersMap.put("search", filtros.getSearch());
        }
        if (filtros.getActivo() != null) {
            filtersMap.put("activo", filtros.getActivo());
        }
        if (filtros.getCodigoSitio() != null) {
            filtersMap.put("codigoSitio", filtros.getCodigoSitio());
        }
        if (filtros.getDescripcion() != null) {
            filtersMap.put("descripcion", filtros.getDescripcion());
        }

        return filtersMap;
    }

    private PageResponseDTO<SiteDTO> toPageResponseDTO(Page<Site> page, Map<String, Object> filters) {
        List<SiteDTO> content = page.getContent().stream()
                .map(this::toSiteDTO)
                .collect(Collectors.toList());

        PageResponseDTO<SiteDTO> response = new PageResponseDTO<>(
                content,
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.getSize()
        );

        response.setFilters(filters);
        return response;
    }

    private PageResponseDTO<SiteDTO> toPageResponseDTO(Page<Site> page) {
        return toPageResponseDTO(page, null);
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

    private void validarCodigoSitio(String codigoSitio, Integer id) {
        if (!StringUtils.hasText(codigoSitio)) {
            throw new RuntimeException("El código de sitio es requerido");
        }

        String codigoTrimmed = codigoSitio.trim();

        // Validación adicional de formato (opcional)
        if (codigoTrimmed.length() > 150) {
            throw new RuntimeException("El código no puede exceder 150 caracteres");
        }

        if (id == null) {
            // Creación - verificar si existe
            if (siteRepository.existsByCodigoSitio(codigoTrimmed)) {
                throw new RuntimeException("El código de sitio '" + codigoTrimmed + "' ya existe. Por favor, use un código diferente.");
            }
        } else {
            // Actualización - verificar si existe en otro registro
            if (siteRepository.existsByCodigoSitioAndIdSiteNot(codigoTrimmed, id)) {
                throw new RuntimeException("El código de sitio '" + codigoTrimmed + "' ya existe en otro registro. Por favor, use un código diferente.");
            }
        }
    }
    private void validarDescripciones(List<SiteDescripcionRequestDTO> descripciones) {
        if (descripciones != null) {
            for (SiteDescripcionRequestDTO desc : descripciones) {
                if (!StringUtils.hasText(desc.getDescripcion()) ||
                        desc.getDescripcion().trim().isEmpty()) {
                    throw new RuntimeException("La descripción no puede estar vacía o contener solo espacios");
                }

                // Validar que no sea solo espacios
                if (desc.getDescripcion().trim().length() == 0) {
                    throw new RuntimeException("La descripción no puede contener solo espacios");
                }
            }
        }
    }
}