package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.OcDetalleRequestDTO;
import com.backend.comfutura.dto.response.OcDetalleResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OcDetalleService {

    // 🔹 Crear o editar un detalle
    OcDetalleResponseDTO guardar(
            Integer idOc,
            Integer idDetalle,
            OcDetalleRequestDTO dto
    );

    // 🔹 Listar detalles por OC (paginado)
    Page<OcDetalleResponseDTO> listarPorOc(
            Integer idOc,
            int page,
            int size
    );

    // 🔹 Reemplazar todos los detalles (bulk)
    void guardarDetalles(
            Integer idOc,
            List<OcDetalleRequestDTO> detalles
    );


}
