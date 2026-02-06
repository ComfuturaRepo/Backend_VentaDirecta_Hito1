package com.backend.comfutura.repository;

import com.backend.comfutura.model.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

    @Query("SELECT a FROM Area a JOIN a.clientes c WHERE c.id = :idCliente AND a.activo = true")
    List<Area> findByClienteIdAndActivoTrue(@Param("idCliente") Integer idCliente);

    List<Area> findByActivoTrueOrderByNombreAsc();
    List<Area> findByIdAreaIn(List<Integer> ids);
    List<Area> findByActivoTrue();
    List<Area> findAllByActivoTrue();


    Optional<Area> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdAreaNot(String nombre, Integer idArea);

    Page<Area> findByActivoTrue(Pageable pageable);

    Page<Area> findByActivoFalse(Pageable pageable);

    Page<Area> findAll(Pageable pageable);

    @Query("SELECT a FROM Area a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Area> search(@Param("search") String search, Pageable pageable);


    @Query("SELECT a FROM Area a JOIN a.clientes c WHERE c.idCliente = :clienteId")
    List<Area> findByClienteId(@Param("clienteId") Integer clienteId);
}