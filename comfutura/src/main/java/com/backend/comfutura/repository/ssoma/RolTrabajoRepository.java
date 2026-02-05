package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.ats.RolTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolTrabajoRepository extends JpaRepository<RolTrabajo, Integer> {
    List<RolTrabajo> findByActivoTrueOrderByNombreAsc();
}