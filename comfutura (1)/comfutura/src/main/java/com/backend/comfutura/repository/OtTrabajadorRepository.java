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

    /**
     * Busca todos los registros de trabajadores para una OT (incluyendo inactivos)
     */
    List<OtTrabajador> findByOts(Ots ots);

    /**
     * Busca solo los trabajadores activos de una OT (usado en detalle completo)
     */
    List<OtTrabajador> findByOtsAndActivoTrue(Ots ots);

    /**
     * Verifica si ya existe asignación de un trabajador específico a la OT
     */
    boolean existsByOtsAndTrabajadorId(Ots ots, Integer trabajadorId);

    /**
     * Elimina todas las asignaciones de trabajadores para una OT
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM OtTrabajador ot WHERE ot.ots = :ots")
    void deleteByOts(@Param("ots") Ots ots);

    /**
     * Conteo de trabajadores activos en una OT
     */
    @Query("SELECT COUNT(ot) FROM OtTrabajador ot WHERE ot.ots = :ots AND ot.activo = true")
    long countActiveByOts(@Param("ots") Ots ots);
}