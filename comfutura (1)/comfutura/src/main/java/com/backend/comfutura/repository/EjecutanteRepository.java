package com.backend.comfutura.repository;

import com.backend.comfutura.model.CoordinadorTiCwPextEnergia;
import com.backend.comfutura.model.Ejecutante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EjecutanteRepository extends JpaRepository<Ejecutante,Integer> {
    List<Ejecutante> findByActivoTrueOrderByDescripcionAsc();
}
