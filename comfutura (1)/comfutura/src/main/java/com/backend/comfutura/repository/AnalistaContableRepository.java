package com.backend.comfutura.repository;

import com.backend.comfutura.model.AnalistaClienteSolicitante;
import com.backend.comfutura.model.AnalistaContable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalistaContableRepository extends JpaRepository<AnalistaContable,Integer> {
    List<AnalistaContable> findByActivoTrueOrderByDescripcionAsc();
}
