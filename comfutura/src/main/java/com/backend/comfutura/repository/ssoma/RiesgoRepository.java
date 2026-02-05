package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.catalogo.Riesgo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiesgoRepository extends JpaRepository<Riesgo, Integer> {
    List<Riesgo> findByPeligroIdPeligroAndActivoTrueOrderByDescripcionAsc(Integer idPeligro);
}