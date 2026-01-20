package com.backend.comfutura.repository;

import com.backend.comfutura.model.Fase;
import com.backend.comfutura.model.Proyecto;
import com.backend.comfutura.model.Region;
import com.backend.comfutura.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaseRepository extends JpaRepository<Fase, Integer> {
    List<Fase> findByActivoTrueOrderByNombreAsc();
}
