package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.CompraDirectaCreateRequest;
import com.backend.comfutura.dto.response.CompraDirectaResponse;
import org.springframework.data.domain.Page;
import com.backend.comfutura.dto.response.CompraDirectaDetalleResponse;

import java.util.List;

public interface CompraDirectaService {


    // CREAR

    CompraDirectaResponse crear(CompraDirectaCreateRequest request);


    // LISTAR (GRILLA)

    Page<CompraDirectaResponse> listar(int page);


    // CAMBIAR ESTADO

    void cambiarEstado(Integer idCompraDirecta, String nuevoEstado, String observacion);


    // DETALLE POR OT

    List<CompraDirectaResponse> obtenerPorOt(Integer ot);
    // 🔥 AGREGA ESTO
    CompraDirectaDetalleResponse obtenerDetalleCompleto(Integer idOts);
}

