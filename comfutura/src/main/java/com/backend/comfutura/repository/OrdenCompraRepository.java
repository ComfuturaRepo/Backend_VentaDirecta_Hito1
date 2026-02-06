package com.backend.comfutura.repository;

import com.backend.comfutura.model.OrdenCompra;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {

    @EntityGraph(attributePaths = {"estadoOC", "ots", "proveedor", "detalles"})
    Page<OrdenCompra> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"estadoOC", "ots", "proveedor", "detalles"})
    Optional<OrdenCompra> findById(Integer idOc);


    @Query("""
 SELECT oc FROM OrdenCompra oc
 JOIN oc.proveedor p
 JOIN oc.ots o
 WHERE
   LOWER(p.razonSocial) LIKE LOWER(CONCAT('%', :search, '%'))
   OR LOWER(o.descripcion) LIKE LOWER(CONCAT('%', :search, '%'))
""")
    Page<OrdenCompra> buscar(@Param("search") String search, Pageable pageable);
}

