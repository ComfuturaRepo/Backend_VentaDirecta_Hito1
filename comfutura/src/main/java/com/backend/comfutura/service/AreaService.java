package com.backend.comfutura.service;


import com.backend.comfutura.dto.Page.PageRequestDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.areaDTO.AreaCreateUpdateDTO;
import com.backend.comfutura.dto.request.areaDTO.AreaDTO;
import com.backend.comfutura.dto.response.areaDTO.AreaSimpleDTO;

import java.util.List;

public interface AreaService {

    PageResponseDTO<AreaDTO> findAll(PageRequestDTO pageRequest);

    PageResponseDTO<AreaDTO> findAllActivos(PageRequestDTO pageRequest);

    PageResponseDTO<AreaDTO> findAllInactivos(PageRequestDTO pageRequest);

    PageResponseDTO<AreaDTO> search(String search, PageRequestDTO pageRequest);

    AreaDTO findById(Integer id);

    AreaDTO create(AreaCreateUpdateDTO areaDTO);

    AreaDTO update(Integer id, AreaCreateUpdateDTO areaDTO);

    AreaDTO toggleStatus(Integer id);

    void delete(Integer id);

    boolean existsByNombre(String nombre);

    List<AreaSimpleDTO> findAllActivosForDropdown();

    List<AreaSimpleDTO> findAllForDropdown();
}