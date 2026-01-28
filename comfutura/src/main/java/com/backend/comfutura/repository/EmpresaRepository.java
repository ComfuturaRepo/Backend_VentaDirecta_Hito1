package com.backend.comfutura.repository;


import com.backend.comfutura.model.Empresa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    List<Empresa> findByActivoTrueOrderByNombreAsc();


    Page<Empresa> findByActivoTrue(PageRequest razonSocial);

    Page<Empresa> findAll(Specification<Empresa> spec, Pageable pageable);
}