package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.catalogo.Herramienta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HerramientaRepository extends JpaRepository<Herramienta, Integer> {
    List<Herramienta> findByActivoTrueOrderByNombreAsc();
}