package com.backend.comfutura.repository;

import com.backend.comfutura.model.OrdenCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdenCompraRepository extends PagingAndSortingRepository<OrdenCompra, Integer> {

    @Query(value = "SELECT oc FROM OrdenCompra oc " +
            "LEFT JOIN FETCH oc.estadoOC " +
            "LEFT JOIN FETCH oc.detalles d " +
            "LEFT JOIN FETCH oc.proveedor p " +
            "LEFT JOIN FETCH oc.ots o",
            countQuery = "SELECT COUNT(oc) FROM OrdenCompra oc")
    Page<OrdenCompra> findAllWithRelations(Pageable pageable);
}
