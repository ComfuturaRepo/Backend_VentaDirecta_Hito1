package com.backend.comfutura.repository;

import com.backend.comfutura.model.OcDetalle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcDetalleRepository extends JpaRepository<OcDetalle, Integer> {

    // 🔹 Paginado
    Page<OcDetalle> findByOrdenCompra_IdOc(Integer idOc, Pageable pageable);

    // 🔹 No paginado (opcional, útil para cálculos)
    List<OcDetalle> findByOrdenCompra_IdOc(Integer idOc);

    // 🔹 Borrado por OC
    void deleteByOrdenCompra_IdOc(Integer idOc);
}