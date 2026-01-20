package com.backend.comfutura.repository;

import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    List<Region> findByActivoTrueOrderByNombreAsc();

}
