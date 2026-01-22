package com.backend.comfutura.repository;

import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.Region;
import com.backend.comfutura.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Integer> {
    List<Region> findByActivoTrueOrderByNombreAsc();

}
