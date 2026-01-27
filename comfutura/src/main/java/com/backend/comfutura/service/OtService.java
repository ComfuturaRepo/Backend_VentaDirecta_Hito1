package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.OtCreateRequest;
import com.backend.comfutura.dto.response.OtDetailResponse;
import com.backend.comfutura.dto.response.OtFullResponse;
import com.backend.comfutura.dto.response.OtListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OtService {

    Page<OtListDto> listarOts(String search, Pageable pageable);

    OtDetailResponse obtenerDetallePorId(Integer idOts);

    OtFullResponse obtenerParaEdicion(Integer idOts);

    OtDetailResponse saveOt(OtCreateRequest request);

    void toggleActivo(Integer idOts);

    List<OtDetailResponse> saveOtsMasivo(List<OtCreateRequest> requests);

    Integer getUltimoOtCorrelativo();

    Integer buscarIdPorOt(Integer ot);

    boolean existeOt(Integer ot);
}