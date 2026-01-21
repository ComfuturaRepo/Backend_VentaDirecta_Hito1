package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.CrearOtCompletaRequest;
import com.backend.comfutura.dto.request.OtCreateRequest;
import com.backend.comfutura.dto.response.OtResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OtService {
    OtResponse createOtCompleta(CrearOtCompletaRequest request);
    Page<OtResponse> listarPorEstado(Boolean activo, Pageable pageable);
    OtResponse obtenerPorId(Integer id);


    void toggleEstado(Integer id);
}