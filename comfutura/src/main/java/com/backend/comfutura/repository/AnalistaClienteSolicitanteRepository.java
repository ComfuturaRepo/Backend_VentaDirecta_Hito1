package com.backend.comfutura.repository;

import com.backend.comfutura.model.AnalistaClienteSolicitante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalistaClienteSolicitanteRepository
        extends JpaRepository<AnalistaClienteSolicitante, Integer>,
        JpaSpecificationExecutor<AnalistaClienteSolicitante> {

    // Métodos existentes
    List<AnalistaClienteSolicitante> findByActivoTrueOrderByDescripcionAsc();

    // Nuevos métodos para paginación
    Page<AnalistaClienteSolicitante> findByActivoTrue(Pageable pageable);
}