package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.PlanillaTrabajoRequest;
import com.backend.comfutura.dto.response.PlanillaTrabajoResponse;

import java.util.List;

public interface PlanillaTrabajoService {

    PlanillaTrabajoResponse crear(PlanillaTrabajoRequest request);

    PlanillaTrabajoResponse editar(Integer idPlanilla, PlanillaTrabajoRequest request);

    List<PlanillaTrabajoResponse> listarPorOt(Integer idOts);
}

