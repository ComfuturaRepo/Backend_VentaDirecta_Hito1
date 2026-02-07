package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.ssoma.SsomaRequestDTO;
import com.backend.comfutura.dto.response.ssomaDTO.SsomaResponseDTO;

import java.util.List;

public interface SsomaService {

    /**
     * Crea todo el formulario SSOMA completo (las 5 hojas)
     */
    SsomaResponseDTO crearFormularioCompleto(SsomaRequestDTO request);

    /**
     * Obtiene todo el formulario SSOMA por ID
     */
    SsomaResponseDTO obtenerFormularioCompleto(Integer idSsoma);

    /**
     * Obtiene todos los formularios SSOMA por OT
     */
    List<SsomaResponseDTO> obtenerFormulariosPorOt(Integer idOts);
}