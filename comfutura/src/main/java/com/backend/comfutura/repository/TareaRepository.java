package com.backend.comfutura.repository;

import com.backend.comfutura.model.ssoma.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TareaRepository extends JpaRepository<Tarea,Integer> {
}
