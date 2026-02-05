package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.catalogo.Epp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EppRepository extends JpaRepository<Epp, Integer> {
    List<Epp> findByActivoTrueOrderByNombreAsc();
}