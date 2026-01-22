package com.backend.comfutura.repository;

import com.backend.comfutura.model.EstadoOc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoOcRepository extends JpaRepository<EstadoOc, Integer> {
}
