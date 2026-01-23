package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.CargoRequestDTO;
import com.backend.comfutura.dto.response.CargoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CargoService {

    CargoResponseDTO guardar(CargoRequestDTO dto);

    Page<CargoResponseDTO> listar(Pageable pageable);

    void toggle(Integer id);
}