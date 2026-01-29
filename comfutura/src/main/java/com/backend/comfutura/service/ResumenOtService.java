package com.backend.comfutura.service;

import com.backend.comfutura.dto.response.ResumenOtResponse;

import java.util.List;

public interface ResumenOtService {

    void recalcularResumen(Integer idOts);

    List<ResumenOtResponse> listarResumen(Integer idOts);
}
