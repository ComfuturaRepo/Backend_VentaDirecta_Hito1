package com.backend.comfutura.repository;

import com.backend.comfutura.model.AnalistaClienteSolicitante;
import com.backend.comfutura.model.JefaturaClienteSolicitante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalistaClienteSolicitanteRepository extends JpaRepository<AnalistaClienteSolicitante,Integer> {
    List<AnalistaClienteSolicitante> findByActivoTrueOrderByDescripcionAsc();

}
