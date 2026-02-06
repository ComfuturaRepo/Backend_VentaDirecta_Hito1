package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageRequestDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.clienteDTO.ClienteCreateUpdateDTO;
import com.backend.comfutura.dto.request.clienteDTO.ClienteDTO;
import com.backend.comfutura.dto.response.clienteDTO.ClienteDetailDTO;

public interface  ClienteService {
    PageResponseDTO<ClienteDTO> findAll(PageRequestDTO pageRequest);

    PageResponseDTO<ClienteDTO> findAllActivos(PageRequestDTO pageRequest);

    PageResponseDTO<ClienteDTO> findAllInactivos(PageRequestDTO pageRequest);

    PageResponseDTO<ClienteDTO> search(String search, PageRequestDTO pageRequest);

    ClienteDetailDTO findById(Integer id);

    ClienteDTO create(ClienteCreateUpdateDTO clienteDTO);

    ClienteDTO update(Integer id, ClienteCreateUpdateDTO clienteDTO);

    ClienteDTO toggleStatus(Integer id);

    void delete(Integer id);

    boolean existsByRuc(String ruc);
}
