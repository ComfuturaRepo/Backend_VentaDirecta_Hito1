package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.TrabajadorFilterDTO;
import com.backend.comfutura.dto.request.trabajadorDTO.TrabajadorRequestDTO;
import com.backend.comfutura.dto.request.trabajadorDTO.TrabajadorUpdateDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorDetailDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorSimpleDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorStatsDTO;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface TrabajadorService {

    PageResponseDTO<TrabajadorSimpleDTO> findAllTrabajadores(Pageable pageable);

    PageResponseDTO<TrabajadorSimpleDTO> findActivos(Pageable pageable);

    PageResponseDTO<TrabajadorSimpleDTO> searchTrabajadores(String search, Pageable pageable);

    PageResponseDTO<TrabajadorSimpleDTO> searchTrabajadoresAdvanced(TrabajadorFilterDTO filterDTO);

    TrabajadorDetailDTO findTrabajadorById(Integer id);

    TrabajadorDetailDTO findTrabajadorByDni(String dni);

    TrabajadorDetailDTO findTrabajadorByEmail(String email);

    TrabajadorDetailDTO createTrabajador(TrabajadorRequestDTO trabajadorDTO);

    TrabajadorDetailDTO updateTrabajador(Integer id, TrabajadorUpdateDTO trabajadorDTO);

    TrabajadorDetailDTO toggleTrabajadorActivo(Integer id);

    TrabajadorStatsDTO getTrabajadorStats();

    long countActivosByArea(Integer areaId);

    long countByCargo(Integer cargoId);

    List<TrabajadorSimpleDTO> getTrabajadoresByFilters(
            String search,
            Boolean activo,
            List<Integer> areaIds,
            List<Integer> cargoIds,
            List<Boolean> roles);
}