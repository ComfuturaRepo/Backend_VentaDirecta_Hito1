package com.backend.comfutura.repository;

import com.backend.comfutura.model.Cargo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer> {
    List<Cargo> findByActivoTrueOrderByNombreAsc();

    Page<Cargo> findByActivoTrue(PageRequest nombre);

    Page<Cargo> findAll(Specification<Cargo> spec, Pageable pageable);
}