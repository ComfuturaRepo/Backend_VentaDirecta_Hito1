

package com.backend.comfutura.repository;

import com.backend.comfutura.model.ViaticoOt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViaticoOtRepository extends JpaRepository<ViaticoOt, Integer> {

    List<ViaticoOt> findByOts_IdOts(Integer idOts);
}
