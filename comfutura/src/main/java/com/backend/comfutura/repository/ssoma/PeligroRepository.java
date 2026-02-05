package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.catalogo.Peligro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeligroRepository extends JpaRepository<Peligro, Integer> {
    List<Peligro> findByActivoTrueOrderByDescripcionAsc();
}
