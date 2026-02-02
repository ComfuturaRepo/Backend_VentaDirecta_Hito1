package com.backend.comfutura.repository;

import com.backend.comfutura.model.OrdenCompraAprobacion;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface OrdenCompraAprobacionRepository
        extends JpaRepository<OrdenCompraAprobacion, Long> {

    Optional<OrdenCompraAprobacion> findByOrdenCompra_IdOcAndNivel(
            Integer idOc,
            Integer nivel
    );

    List<OrdenCompraAprobacion> findByOrdenCompra_IdOcOrderByNivel(
            Integer idOc
    );
}

