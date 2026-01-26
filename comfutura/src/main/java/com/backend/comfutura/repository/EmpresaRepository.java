package com.backend.comfutura.repository;


import com.backend.comfutura.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    List<Empresa> findByActivoTrueOrderByNombreAsc();

}