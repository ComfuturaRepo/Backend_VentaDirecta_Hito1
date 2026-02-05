package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.catalogo.MedidaControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedidaControlRepository extends JpaRepository<MedidaControl, Integer> {
    List<MedidaControl> findByRiesgoIdRiesgoAndActivoTrueOrderByDescripcionAsc(Integer idRiesgo);
}