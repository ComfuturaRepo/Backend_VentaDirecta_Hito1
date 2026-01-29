package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.model.EstadoCompraDirecta;
import com.backend.comfutura.repository.EstadoCompraDirectaRepository;
import com.backend.comfutura.service.EstadoCompraDirectaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadoCompraDirectaServiceImpl
        implements EstadoCompraDirectaService {

    private final EstadoCompraDirectaRepository repository;

    @Override
    public List<EstadoCompraDirecta> listarActivos() {
        return repository.findByActivoTrueOrderByDescripcionAsc();
    }

    @Override
    public EstadoCompraDirecta buscarPorDescripcion(String descripcion) {
        return repository.findByDescripcion(descripcion)
                .orElseThrow(() ->
                        new RuntimeException("Estado no encontrado: " + descripcion)
                );
    }
}




