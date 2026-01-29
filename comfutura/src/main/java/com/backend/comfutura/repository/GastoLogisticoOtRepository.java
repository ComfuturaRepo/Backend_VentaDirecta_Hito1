package com.backend.comfutura.repository;

import com.backend.comfutura.model.GastoLogisticoOt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GastoLogisticoOtRepository
        extends JpaRepository<GastoLogisticoOt, Integer> {

    List<GastoLogisticoOt> findByOts_IdOts(Integer idOts);
}


