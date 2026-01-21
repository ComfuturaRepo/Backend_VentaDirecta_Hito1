package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.CrearOtCompletaRequest;
import com.backend.comfutura.dto.response.OtFullResponse;
import com.backend.comfutura.dto.response.OtResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OtService {

    /**
     * Crea una nueva OT o actualiza una existente (upsert)
     * @param request Contiene todos los datos de la OT + lista de trabajadores
     * @return OtResponse básico con id, número de OT, etc.
     */
    OtResponse saveOtCompleta(CrearOtCompletaRequest request);

    /**
     * Lista las OTs con filtros flexibles:
     * - Si se envía 'ot' → busca exactamente esa OT (ignora activo)
     * - Si activo = null → devuelve todas (activas e inactivas)
     * - Si activo = true/false → filtra por estado
     */
    Page<OtResponse> listarOts(Integer ot, Boolean activo, Pageable pageable);

    /**
     * Obtiene la información básica de una OT por su ID interno (id_ots)
     */
    OtResponse obtenerPorId(Integer id);

    /**
     * Obtiene TODA la información de una OT para su edición en frontend
     * (incluye todos los IDs de referencias, descripción, responsables y lista completa de trabajadores asignados)
     */
    OtFullResponse getOtForEdit(Integer id);

    /**
     * Cambia el estado activo ↔ inactivo de la OT
     */
    void toggleEstado(Integer id);
}