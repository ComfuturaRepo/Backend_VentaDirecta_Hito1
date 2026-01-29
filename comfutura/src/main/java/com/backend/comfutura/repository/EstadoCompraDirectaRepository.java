package com.backend.comfutura.repository;

import com.backend.comfutura.model.EstadoCompraDirecta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstadoCompraDirectaRepository
        extends JpaRepository<EstadoCompraDirecta, Integer> {

    // Buscar por descripción (REGISTRADO, APROBADO, etc.)
    Optional<EstadoCompraDirecta> findByDescripcion(String descripcion);

    // Listar estados activos
    List<EstadoCompraDirecta> findByActivoTrueOrderByDescripcionAsc();
}
