package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.ats.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TareaRepository extends JpaRepository<Tarea, Integer> {
    List<Tarea> findByTrabajoIdTrabajoAndActivoTrueOrderByDescripcionAsc(Integer idTrabajo);
    List<Tarea> findByActivoTrueOrderByDescripcionAsc();
}
