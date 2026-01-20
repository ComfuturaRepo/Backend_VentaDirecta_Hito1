package com.backend.comfutura.repository;

import com.backend.comfutura.model.JefaturaResponsable;
import com.backend.comfutura.model.Liquidador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiquidadorRepository extends JpaRepository<Liquidador,Integer> {
    List<Liquidador> findByActivoTrueOrderByDescripcionAsc();
}
