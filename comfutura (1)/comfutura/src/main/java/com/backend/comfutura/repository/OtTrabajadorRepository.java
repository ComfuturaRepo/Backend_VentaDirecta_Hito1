package com.backend.comfutura.repository;

import com.backend.comfutura.model.OtTrabajador;
import com.backend.comfutura.model.Ots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OtTrabajadorRepository extends JpaRepository<OtTrabajador, Integer> {

    List<OtTrabajador> findByOts(Ots ots);

    boolean existsByOtsAndTrabajadorId(Ots ots, Integer trabajadorId);

    @Modifying
    @Transactional
    @Query("DELETE FROM OtTrabajador ot WHERE ot.ots = :ots")
    void deleteByOts(@Param("ots") Ots ots);

    // Opcional
    @Query("SELECT COUNT(ot) FROM OtTrabajador ot WHERE ot.ots = :ots AND ot.activo = true")
    long countActiveByOts(@Param("ots") Ots ots);
}