package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.OrdenCompraRequestDTO;
import com.backend.comfutura.dto.response.OrdenCompraResponseDTO;
import org.springframework.data.domain.Page;

public interface OrdenCompraService {

    OrdenCompraResponseDTO guardar(Integer idOc, OrdenCompraRequestDTO dto);

    Page<OrdenCompraResponseDTO> listarPaginado(
            int page,
            int size,
            String sortBy,
            String direction
    );



    // 🔹 HTML con empresa seleccionada (NUEVO)
    String generarHtml(Integer idOc, Integer idEmpresa);

}
