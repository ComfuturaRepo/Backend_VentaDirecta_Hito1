package com.backend.comfutura.repository;

import com.backend.comfutura.model.AnalistaContable;
import com.backend.comfutura.model.CoordinadorTiCwPextEnergia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CoordinadorTiCwRepository extends JpaRepository<CoordinadorTiCwPextEnergia,Integer> {
    List<CoordinadorTiCwPextEnergia> findByActivoTrueOrderByDescripcionAsc();
}
