package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.ats.AtsRiesgo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtsRiesgoRepository extends JpaRepository<AtsRiesgo, Integer> {
}
