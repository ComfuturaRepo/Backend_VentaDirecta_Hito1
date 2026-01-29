package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.ViaticoRequest;
import com.backend.comfutura.dto.response.ViaticoResponse;

import java.util.List;

public interface ViaticoService {

    ViaticoResponse crear(ViaticoRequest request);

    ViaticoResponse editar(Integer idViatico, ViaticoRequest request);

    List<ViaticoResponse> listarPorOt(Integer idOts);
}