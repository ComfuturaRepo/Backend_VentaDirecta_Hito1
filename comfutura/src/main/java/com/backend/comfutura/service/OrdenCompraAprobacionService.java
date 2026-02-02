package com.backend.comfutura.service;



import com.backend.comfutura.dto.request.OrdenCompraAprobacionRequest;
import com.backend.comfutura.dto.response.OrdenCompraAprobacionResponse;

import java.util.List;

public interface OrdenCompraAprobacionService {

    OrdenCompraAprobacionResponse aprobar(
            Integer idOc,
            Integer nivel,
            String estado,
            OrdenCompraAprobacionRequest request
    );

    List<OrdenCompraAprobacionResponse> obtenerHistorial(Integer idOc);
}


