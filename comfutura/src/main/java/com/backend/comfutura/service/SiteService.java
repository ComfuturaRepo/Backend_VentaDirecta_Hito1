package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SiteService {

    // Paginados con PageResponseDTO
    PageResponseDTO<Site> listar(Pageable pageable);
    PageResponseDTO<Site> listarActivos(Pageable pageable);
    PageResponseDTO<Site> buscar(String search, Pageable pageable);

    // Para mantener compatibilidad
    Page<Site> listarPaginado(Pageable pageable);

    // CRUD
    Site guardar(Site site);
    Site obtenerPorId(Integer id);
    void toggle(Integer id);

    // Métodos de validación
    boolean existeCodigoSitio(String codigoSitio);
    boolean existeCodigoSitioConOtroId(String codigoSitio, Integer id);
}