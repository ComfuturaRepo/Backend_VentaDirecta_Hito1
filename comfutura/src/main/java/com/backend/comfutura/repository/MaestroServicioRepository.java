package com.backend.comfutura.repository;

import com.backend.comfutura.model.MaestroServicio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaestroServicioRepository extends JpaRepository<MaestroServicio, Integer> {

    // Paginación
    Page<MaestroServicio> findByActivoTrueOrderByCodigoAsc(Pageable pageable);

    // Para dropdown
    List<MaestroServicio> findByActivoTrueOrderByCodigoAsc();
}