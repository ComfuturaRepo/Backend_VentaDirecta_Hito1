package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.ContratistaRequest;
import com.backend.comfutura.dto.response.ContratistaResponse;

import java.util.List;

public interface ContratistaService {

    ContratistaResponse crear(ContratistaRequest request);

    ContratistaResponse editar(Integer idContratistaOt, ContratistaRequest request);

    List<ContratistaResponse> listarPorOt(Integer idOts);
}
