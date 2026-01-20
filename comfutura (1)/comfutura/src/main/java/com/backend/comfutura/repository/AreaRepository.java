package com.backend.comfutura.repository;

import com.backend.comfutura.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

    @Query("SELECT a FROM Area a JOIN a.clientes c WHERE c.id = :idCliente AND a.activo = true")
    List<Area> findByClienteIdAndActivoTrue(@Param("idCliente") Integer idCliente);

    List<Area> findAllByActivoTrue();
}