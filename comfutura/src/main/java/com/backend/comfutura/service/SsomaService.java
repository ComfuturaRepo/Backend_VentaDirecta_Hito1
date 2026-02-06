package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.ssomaDTO.*;
import com.backend.comfutura.dto.response.ssomaDTO.*;
import java.util.List;

public interface SsomaService {
    SsomaCompletoResponse crearSsomaCompleto(SsomaCompletoRequest request);
    SsomaCompletoResponse obtenerSsomaCompletoPorOts(Integer idOts);
    // ATS
    AtsResponse crearAts(AtsRequest request);
    AtsResponse obtenerAtsPorId(Integer id);
    List<AtsResponse> listarTodosAts();

    // Capacitación
    CapacitacionResponse crearCapacitacion(CapacitacionRequest request);
    CapacitacionResponse obtenerCapacitacionPorId(Integer id);
    List<CapacitacionResponse> listarTodasCapacitaciones();

    // Inspección EPP
    InspeccionEppResponse crearInspeccionEpp(InspeccionEppRequest request);
    InspeccionEppResponse obtenerInspeccionEppPorId(Integer id);
    List<InspeccionEppResponse> listarTodasInspeccionesEpp();

    // Inspección Herramientas
    InspeccionHerramientaResponse crearInspeccionHerramienta(InspeccionHerramientaRequest request);
    InspeccionHerramientaResponse obtenerInspeccionHerramientaPorId(Integer id);
    List<InspeccionHerramientaResponse> listarTodasInspeccionesHerramientas();

    // PETAR
    PetarResponse crearPetar(PetarRequest request);
    PetarResponse obtenerPetarPorId(Integer id);
    List<PetarResponse> listarTodosPetar();
}