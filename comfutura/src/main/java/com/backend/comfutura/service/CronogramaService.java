package com.backend.comfutura.service;


import com.backend.comfutura.dto.request.CronogramaRequest;
import com.backend.comfutura.dto.response.CronogramaResponse;


import java.util.List;


public interface CronogramaService {


    // ➕ crear partida de cronograma
    CronogramaResponse crear(CronogramaRequest request);


    // ✏️ editar partida
    CronogramaResponse editar(Integer idCronograma, CronogramaRequest request);


    // 📄 listar cronograma por OT
    List<CronogramaResponse> listarPorOt(Integer idOts);
}

