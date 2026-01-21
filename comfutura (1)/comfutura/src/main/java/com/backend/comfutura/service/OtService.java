package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.CrearOtCompletaRequest;
import com.backend.comfutura.dto.response.OtFullDetailResponse;
import com.backend.comfutura.dto.response.OtResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OtService {

    Page<OtResponse> listarOts(Boolean activo, Pageable pageable);

    OtFullDetailResponse obtenerDetalleCompleto(Integer idOts);

    OtFullDetailResponse obtenerPorNumeroOt(Integer numeroOt);

    OtResponse saveOtCompleta(CrearOtCompletaRequest request);

    void toggleEstado(Integer idOts);

    // Opcional: si aún lo necesitas para compatibilidad
    OtResponse obtenerPorId(Integer id);
}