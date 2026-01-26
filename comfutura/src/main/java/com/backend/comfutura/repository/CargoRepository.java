package com.backend.comfutura.repository;

import com.backend.comfutura.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {
    List<Cargo> findByActivoTrueOrderByNombreAsc();

}