package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.otDTO.OtCreateRequest;
import com.backend.comfutura.dto.response.otDTO.OtDetailResponse;
import com.backend.comfutura.dto.response.otDTO.OtFullResponse;
import com.backend.comfutura.dto.response.otDTO.OtListDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OtService {

    PageResponseDTO<OtListDto> listarOts(String search, Pageable pageable);

    OtDetailResponse obtenerDetallePorId(Integer idOts);

    OtFullResponse obtenerParaEdicion(Integer idOts);

    OtDetailResponse saveOt(OtCreateRequest request);

    void toggleActivo(Integer idOts);

    List<OtDetailResponse> saveOtsMasivo(List<OtCreateRequest> requests);

    Integer getUltimoOtCorrelativo();

    Integer buscarIdPorOt(Integer ot);

    boolean existeOt(Integer ot);
}