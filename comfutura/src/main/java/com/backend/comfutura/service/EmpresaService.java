package com.backend.comfutura.service;


import com.backend.comfutura.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmpresaService {

    List<Empresa> listar();

    Page<Empresa> listarPaginado(Pageable pageable);

    Empresa obtenerPorId(Integer id);

    Empresa guardar(Empresa empresa);

    Empresa toggleActivo(Integer id);
}