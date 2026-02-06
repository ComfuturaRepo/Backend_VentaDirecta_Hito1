package com.backend.comfutura.repository;

import com.backend.comfutura.model.TipoOt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoOtRepository extends JpaRepository<TipoOt, Integer> {

    Optional<TipoOt> findByCodigo(String codigo);
    List<TipoOt> findByActivoTrueOrderByCodigoAsc();
    List<TipoOt> findByActivoTrue();

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdTipoOtNot(String codigo, Integer idTipoOt);
}