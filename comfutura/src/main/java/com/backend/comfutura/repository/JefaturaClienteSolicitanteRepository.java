package com.backend.comfutura.repository;

import com.backend.comfutura.model.JefaturaClienteSolicitante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JefaturaClienteSolicitanteRepository extends JpaRepository<JefaturaClienteSolicitante,Integer> {
    List<JefaturaClienteSolicitante> findByActivoTrueOrderByDescripcionAsc();
}
