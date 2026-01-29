package com.backend.comfutura.repository;

import com.backend.comfutura.model.CompraDirecta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompraDirectaRepository
        extends JpaRepository<CompraDirecta, Integer> {

    // Buscar por número de requerimiento
    Optional<CompraDirecta> findByNroRequerimiento(String nroRequerimiento);

    // Listado para grilla
    Page<CompraDirecta> findByActivoTrue(Pageable pageable);

    // Detalle por número de OT (campo OT)
    Optional<CompraDirecta> findByOts_Ot(Integer ot);

    // Detalle por ID de OT (PK) — UNA sola
    Optional<CompraDirecta> findByOts_IdOts(Integer idOts);

    //  NUEVO — LISTA de compras directas por OT
    List<CompraDirecta> findAllByOts_IdOtsAndActivoTrue(Integer idOts);
}
