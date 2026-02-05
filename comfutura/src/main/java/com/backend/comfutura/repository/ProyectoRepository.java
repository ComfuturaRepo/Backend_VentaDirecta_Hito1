package com.backend.comfutura.repository;

import com.backend.comfutura.model.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List; //

public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {

    // Para paginación
    Page<Proyecto> findByActivoTrueOrderByNombreAsc(Pageable pageable);

    // Para dropdown
    List<Proyecto> findByActivoTrueOrderByNombreAsc();

    Page<Proyecto> findAll(Specification<Proyecto> spec, Pageable pageable);


    // Verificar si existe un proyecto con el mismo nombre (case-insensitive)
    @Query("SELECT COUNT(p) > 0 FROM Proyecto p WHERE LOWER(p.nombre) = LOWER(:nombre)")
    boolean existsByNombreIgnoreCase(@Param("nombre") String nombre);

    // Verificar si existe otro proyecto con el mismo nombre (excluyendo uno)
    @Query("SELECT COUNT(p) > 0 FROM Proyecto p WHERE LOWER(p.nombre) = LOWER(:nombre) AND p.idProyecto != :idProyecto")
    boolean existsByNombreAndIdProyectoNot(@Param("nombre") String nombre, @Param("idProyecto") Integer idProyecto);
}