
package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.model.AnalistaClienteSolicitante;
import com.backend.comfutura.repository.AnalistaClienteSolicitanteRepository;
import com.backend.comfutura.service.AnalistaClienteSolicitanteService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalistaClienteSolicitanteServiceImpl
        implements AnalistaClienteSolicitanteService {

    private final AnalistaClienteSolicitanteRepository repository;

    // CREAR + EDITAR
    @Override
    @Transactional
    public AnalistaClienteSolicitante guardar(AnalistaClienteSolicitante analista) {

        if (analista.getId() == null) {
            // CREAR
            analista.setActivo(true);
        } else {
            // EDITAR
            AnalistaClienteSolicitante db = repository.findById(analista.getId())
                    .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

            db.setDescripcion(analista.getDescripcion());
            return repository.save(db);
        }

        return repository.save(analista);
    }

    // LISTAR
    @Override
    @Transactional(readOnly = true)
    public List listar() {
        return (List) repository.findByActivoTrueOrderByDescripcionAsc();
    }

    // TOGGLE
    @Override
    @Transactional
    public void toggle(Integer id) {

        AnalistaClienteSolicitante analista = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));

        analista.setActivo(!analista.getActivo());
        repository.save(analista);
    }
}