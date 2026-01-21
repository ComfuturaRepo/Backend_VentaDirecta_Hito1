package com.backend.comfutura.repository;

import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtsRepository extends JpaRepository<Ots, Integer> {

    // Verifica si ya existe una OT con ese número
    boolean existsByOt(Integer ot);
    Optional<Ots> findAllByActivoTrue();

    // Para dropdowns (solo activos)
    List<Ots> findByActivoTrueOrderByOtAsc();
    Optional<Ots> findTopByOrderByOtDesc();

    Page<Ots> findByActivoTrue(Pageable pageable);

    Page<Ots> findByActivo(Boolean activo, Pageable pageable);

}

