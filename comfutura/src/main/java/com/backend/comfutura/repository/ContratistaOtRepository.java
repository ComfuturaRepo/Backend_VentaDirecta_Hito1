package com.backend.comfutura.repository;

import com.backend.comfutura.model.ContratistaOt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratistaOtRepository
        extends JpaRepository<ContratistaOt, Integer> {

    List<ContratistaOt> findByOts_IdOts(Integer idOts);
}
