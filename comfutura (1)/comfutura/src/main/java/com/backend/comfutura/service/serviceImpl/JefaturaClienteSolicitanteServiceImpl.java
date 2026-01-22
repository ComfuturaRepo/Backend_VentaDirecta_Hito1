package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.model.JefaturaClienteSolicitante;
import com.backend.comfutura.repository.JefaturaClienteSolicitanteRepository;
import com.backend.comfutura.service.JefaturaClienteSolicitanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JefaturaClienteSolicitanteServiceImpl
        implements JefaturaClienteSolicitanteService {

    private final JefaturaClienteSolicitanteRepository repository;

    // CREAR + EDITAR
    @Override
    @Transactional
    public JefaturaClienteSolicitante guardar(JefaturaClienteSolicitante jefatura) {

        if (jefatura.getId() == null) {
            jefatura.setActivo(true);
            return repository.save(jefatura);
        }

        JefaturaClienteSolicitante db = repository.findById(jefatura.getId())
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        db.setDescripcion(jefatura.getDescripcion());
        return repository.save(db);
    }

    // LISTAR SOLO ACTIVOS
    @Override
    @Transactional(readOnly = true)
    public List<JefaturaClienteSolicitante> listar() {
        return repository.findAll();
    }

    // TOGGLE ACTIVO / INACTIVO
    @Override
    @Transactional
    public void toggle(Integer id) {

        JefaturaClienteSolicitante jefatura = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        jefatura.setActivo(!jefatura.getActivo());
        repository.save(jefatura);
    }
}