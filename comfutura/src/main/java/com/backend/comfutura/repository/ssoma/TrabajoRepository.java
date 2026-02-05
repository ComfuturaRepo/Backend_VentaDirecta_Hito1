package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.ats.Trabajo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrabajoRepository extends JpaRepository<Trabajo, Integer> {
    List<Trabajo> findByActivoTrueOrderByNombreAsc();
}

