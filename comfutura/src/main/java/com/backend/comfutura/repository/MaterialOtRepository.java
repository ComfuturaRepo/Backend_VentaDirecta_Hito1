package com.backend.comfutura.repository;

import com.backend.comfutura.model.MaterialOt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialOtRepository
        extends JpaRepository<MaterialOt, Integer> {

    // 📄 Listar materiales por OT
    List<MaterialOt> findByOts_IdOts(Integer idOts);
}

