package com.backend.comfutura.service;

import com.backend.comfutura.dto.*;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.TrabajadorRequestDTO;
import com.backend.comfutura.dto.request.TrabajadorUpdateDTO;
import com.backend.comfutura.dto.response.TrabajadorDetailDTO;
import com.backend.comfutura.dto.response.TrabajadorSimpleDTO;
import com.backend.comfutura.dto.response.TrabajadorStatsDTO;
import org.springframework.data.domain.Pageable;

public interface TrabajadorService {

    // Listar con paginación y filtros
    PageResponseDTO<TrabajadorSimpleDTO> findAllTrabajadores(Pageable pageable);

    PageResponseDTO<TrabajadorSimpleDTO> findTrabajadoresActivos(Pageable pageable);
    PageResponseDTO<TrabajadorSimpleDTO> searchTrabajadores(String search, Pageable pageable);
    PageResponseDTO<TrabajadorSimpleDTO> searchTrabajadores(
            String search,
            Boolean activo,
            Integer areaId,
            Integer cargoId,
            Integer empresaId,
            Pageable pageable);

    // Obtener por ID
    TrabajadorDetailDTO findTrabajadorById(Integer id);
    PageResponseDTO<TrabajadorSimpleDTO> findActivos(Pageable pageable) ;
    // Obtener por DNI
    TrabajadorDetailDTO findTrabajadorByDni(String dni);

    // Crear
    TrabajadorDetailDTO createTrabajador(TrabajadorRequestDTO trabajadorDTO);

    // Actualizar
    TrabajadorDetailDTO updateTrabajador(Integer id, TrabajadorUpdateDTO trabajadorDTO);

    // Activar/Desactivar (toggle)
    TrabajadorDetailDTO toggleTrabajadorActivo(Integer id);

    // Estadísticas
    TrabajadorStatsDTO getTrabajadorStats();

    // Contar por área
    long countActivosByArea(Integer areaId);

    // Contar por cargo
    long countByCargo(Integer cargoId);
}