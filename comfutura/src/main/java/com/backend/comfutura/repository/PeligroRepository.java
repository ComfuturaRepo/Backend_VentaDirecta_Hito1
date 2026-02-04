package com.backend.comfutura.repository;

import com.backend.comfutura.model.ssoma.Peligro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeligroRepository extends JpaRepository<Peligro,Integer> {
}
