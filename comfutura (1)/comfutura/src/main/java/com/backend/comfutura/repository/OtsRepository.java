package com.backend.comfutura.repository;

import com.backend.comfutura.model.Ots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OtsRepository extends JpaRepository<Ots, Integer> {

    // Verifica si ya existe una OT con ese número
    boolean existsByOt(Integer ot);

    // Para dropdowns (solo activos)
    List<Ots> findByActivoTrueOrderByOtAsc();
}

