package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.SSOMA.ATSRequestDTO;
import com.backend.comfutura.dto.request.SSOMA.SSTFormRequestDTO;
import com.backend.comfutura.dto.response.SSOMA.ATSListDTO;
import com.backend.comfutura.dto.response.SSOMA.ATSResponseDTO;
import com.backend.comfutura.dto.response.SSOMA.SSTFormResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ATSService {
    PageResponseDTO<ATSListDTO> findAllPaginado(Pageable pageable, String search);
    ATSResponseDTO findById(Integer id);
    ATSResponseDTO create(ATSRequestDTO atsRequestDTO);
    SSTFormResponseDTO crearFormularioCompleto(SSTFormRequestDTO requestDTO);
}