package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmpresaService {
    // Paginados
    PageResponseDTO<Empresa> listar(Pageable pageable);
    PageResponseDTO<Empresa> listarActivos(Pageable pageable);
    PageResponseDTO<Empresa> buscar(String search, Pageable pageable);

    // Para mantener compatibilidad
    List<Empresa> listar();
    Page<Empresa> listarPaginado(Pageable pageable);

    // CRUD
    Empresa obtenerPorId(Integer id);
    Empresa guardar(Empresa empresa);
    Empresa toggleActivo(Integer id);
}