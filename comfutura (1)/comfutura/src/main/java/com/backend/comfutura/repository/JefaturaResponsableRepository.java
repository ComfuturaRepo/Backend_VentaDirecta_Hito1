package com.backend.comfutura.repository;

import com.backend.comfutura.model.Ejecutante;
import com.backend.comfutura.model.JefaturaResponsable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JefaturaResponsableRepository extends JpaRepository<JefaturaResponsable,Integer> {
    List<JefaturaResponsable> findByActivoTrueOrderByDescripcionAsc();
}
