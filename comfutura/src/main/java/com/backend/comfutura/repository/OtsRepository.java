package com.backend.comfutura.repository;

import com.backend.comfutura.model.Ots;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;  // ← AGREGAR ESTA IMPORTACIÓN
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OtsRepository extends JpaRepository<Ots, Integer>, JpaSpecificationExecutor<Ots> {

    Optional<Ots> findByOt(Integer ot);

    Page<Ots> findByActivo(Boolean activo, Pageable pageable);

    Optional<Ots> findTopByOrderByOtDesc();

    @Query("SELECT o FROM Ots o " +
            "LEFT JOIN FETCH o.cliente " +
            "LEFT JOIN FETCH o.area " +
            "LEFT JOIN FETCH o.proyecto " +
            "LEFT JOIN FETCH o.fase " +
            "LEFT JOIN FETCH o.site " +
            "LEFT JOIN FETCH o.region " +
            "LEFT JOIN FETCH o.estadoOt " +
            "LEFT JOIN FETCH o.coordinadorTiCw " +
            "LEFT JOIN FETCH o.jefaturaResponsable " +
            "LEFT JOIN FETCH o.liquidador " +
            "LEFT JOIN FETCH o.ejecutante " +
            "LEFT JOIN FETCH o.analistaContable " +
            "LEFT JOIN FETCH o.jefaturaClienteSolicitante " +
            "LEFT JOIN FETCH o.analistaClienteSolicitante " +
            "WHERE o.idOts = :idOts")
    Optional<Ots> findByIdWithAllRelations(@Param("idOts") Integer idOts);

    @Query("SELECT o FROM Ots o " +
            "LEFT JOIN FETCH o.cliente " +
            "LEFT JOIN FETCH o.area " +
            "LEFT JOIN FETCH o.proyecto " +
            "LEFT JOIN FETCH o.fase " +
            "LEFT JOIN FETCH o.site " +
            "LEFT JOIN FETCH o.region " +
            "LEFT JOIN FETCH o.estadoOt " +
            "LEFT JOIN FETCH o.coordinadorTiCw " +
            "LEFT JOIN FETCH o.jefaturaResponsable " +
            "LEFT JOIN FETCH o.liquidador " +
            "LEFT JOIN FETCH o.ejecutante " +
            "LEFT JOIN FETCH o.analistaContable " +
            "LEFT JOIN FETCH o.jefaturaClienteSolicitante " +
            "LEFT JOIN FETCH o.analistaClienteSolicitante " +
            "WHERE o.ot = :ot")
    Optional<Ots> findByOtWithAllRelations(@Param("ot") Integer ot);

    List<Ots> findByActivoTrueOrderByOtAsc();
}