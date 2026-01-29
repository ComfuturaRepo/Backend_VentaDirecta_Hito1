package com.backend.comfutura.repository;

import com.backend.comfutura.model.ResumenOt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumenOtRepository
        extends JpaRepository<ResumenOt, Integer> {

    List<ResumenOt> findByOts_IdOts(Integer idOts);

    Optional<ResumenOt> findByOts_IdOtsAndTipoGasto(Integer idOts, String tipoGasto);
}


