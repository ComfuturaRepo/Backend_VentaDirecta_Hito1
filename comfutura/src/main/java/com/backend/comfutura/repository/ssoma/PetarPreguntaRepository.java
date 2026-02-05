package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.petar.PetarPregunta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetarPreguntaRepository extends JpaRepository<PetarPregunta, Integer> {
    List<PetarPregunta> findByActivoTrueOrderByDescripcionAsc();
}