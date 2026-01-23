package com.backend.comfutura.repository;

import com.backend.comfutura.model.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List; //

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {

    // Para paginación
    Page<Proyecto> findByActivoTrueOrderByNombreAsc(Pageable pageable);

    // Para dropdown
    List<Proyecto> findByActivoTrueOrderByNombreAsc();

}