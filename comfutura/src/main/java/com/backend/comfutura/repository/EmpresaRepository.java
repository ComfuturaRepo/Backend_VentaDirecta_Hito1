package com.backend.comfutura.repository;


import com.backend.comfutura.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    List<Empresa> findByActivoTrueOrderByNombreAsc();
    Optional<Empresa> findByActivoTrue();


}