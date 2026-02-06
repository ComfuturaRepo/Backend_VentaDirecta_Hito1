package com.backend.comfutura.repository;

import com.backend.comfutura.model.JefaturaClienteSolicitante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JefaturaClienteSolicitanteRepository extends JpaRepository<JefaturaClienteSolicitante,Integer> {
    List<JefaturaClienteSolicitante> findByActivoTrueOrderByDescripcionAsc();

    Page<JefaturaClienteSolicitante> findByActivoTrue(PageRequest descripcion);

    Page<JefaturaClienteSolicitante> findAll(Specification<JefaturaClienteSolicitante> spec, Pageable pageable);
}
