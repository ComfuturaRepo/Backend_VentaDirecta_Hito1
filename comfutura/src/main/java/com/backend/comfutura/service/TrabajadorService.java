package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.trabajadorDTO.TrabajadorRequestDTO;
import com.backend.comfutura.dto.request.trabajadorDTO.TrabajadorUpdateDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorDetailDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorSimpleDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorStatsDTO;
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