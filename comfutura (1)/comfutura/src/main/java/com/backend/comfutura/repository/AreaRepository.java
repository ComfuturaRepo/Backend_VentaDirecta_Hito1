package com.backend.comfutura.repository;

import com.backend.comfutura.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {
    List<Area> findByActivoTrueOrderByNombreAsc();
}
