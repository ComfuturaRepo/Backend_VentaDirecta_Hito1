package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.Page.sitePage.SiteFilterDTO;
import com.backend.comfutura.dto.request.SiteRequestDTO;
import com.backend.comfutura.dto.response.SiteDTO;
import org.springframework.data.domain.Pageable;

public interface SiteService {
    PageResponseDTO<SiteDTO> listar(Pageable pageable);
    PageResponseDTO<SiteDTO> listarActivos(Pageable pageable);
    PageResponseDTO<SiteDTO> buscar(String search, Pageable pageable);
    PageResponseDTO<SiteDTO> listarConFiltros(SiteFilterDTO filtros, Pageable pageable);
    SiteDTO obtenerPorId(Integer id);
    SiteDTO guardar(SiteRequestDTO request);
    SiteDTO actualizar(Integer id, SiteRequestDTO request);
    void toggle(Integer id);
    boolean existeCodigoSitio(String codigoSitio);
    boolean existeCodigoSitioConOtroId(String codigoSitio, Integer id);
}