package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.model.Empresa;
import com.backend.comfutura.repository.EmpresaRepository;
import com.backend.comfutura.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaServiceImpl implements EmpresaService {

    private final EmpresaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Empresa> listar() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empresa> listarPaginado(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Empresa obtenerPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
    }

    @Override
    @Transactional
    public Empresa guardar(Empresa empresa) {
        return repository.save(empresa);
    }

    @Override
    @Transactional
    public Empresa toggleActivo(Integer id) {
        Empresa empresa = obtenerPorId(id);
        empresa.setActivo(!empresa.getActivo());
        return repository.save(empresa);
    }
}