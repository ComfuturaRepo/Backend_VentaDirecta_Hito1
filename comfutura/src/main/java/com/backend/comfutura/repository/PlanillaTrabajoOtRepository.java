package com.backend.comfutura.repository;

import com.backend.comfutura.model.PlanillaTrabajoOt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanillaTrabajoOtRepository
        extends JpaRepository<PlanillaTrabajoOt, Integer> {

    List<PlanillaTrabajoOt> findByOts_IdOts(Integer idOts);
}
