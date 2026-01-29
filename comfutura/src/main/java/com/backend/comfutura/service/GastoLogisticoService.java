package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.GastoLogisticoRequest;
import com.backend.comfutura.dto.response.GastoLogisticoResponse;

import java.util.List;

public interface GastoLogisticoService {

    GastoLogisticoResponse crear(GastoLogisticoRequest request);

    GastoLogisticoResponse editar(Integer idGastoLog, GastoLogisticoRequest request);

    List<GastoLogisticoResponse> listarPorOt(Integer idOts);
}
