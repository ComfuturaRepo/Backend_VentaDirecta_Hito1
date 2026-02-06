package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.CargoRequestDTO;
import com.backend.comfutura.dto.response.CargoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CargoService {
    // Paginados
    PageResponseDTO<CargoResponseDTO> listar(Pageable pageable);
    PageResponseDTO<CargoResponseDTO> listarActivos(Pageable pageable);
    PageResponseDTO<CargoResponseDTO> buscar(String search, Pageable pageable);

    // Para mantener compatibilidad
    Page<CargoResponseDTO> listarPaginado(Pageable pageable);

    // CRUD
    CargoResponseDTO guardar(CargoRequestDTO dto);
    void toggle(Integer id);
}