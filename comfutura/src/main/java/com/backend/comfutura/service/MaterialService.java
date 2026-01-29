package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.MaterialRequest;
import com.backend.comfutura.dto.response.MaterialResponse;


import java.util.List;


public interface MaterialService {


    // ➕ crear material por OT
    MaterialResponse crear(MaterialRequest request);


    // ✏️ editar material
    MaterialResponse editar(Integer idMaterialOt, MaterialRequest request);


    // 📄 listar materiales por OT
    List<MaterialResponse> listarPorOt(Integer idOts);
}

