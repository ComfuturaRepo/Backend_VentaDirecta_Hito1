package com.backend.comfutura.repository;

import com.backend.comfutura.dto.response.CronogramaResponse;
import com.backend.comfutura.model.CronogramaOt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CronogramaOtRepository
        extends JpaRepository<CronogramaOt, Integer> {

    // =========================
    // LISTAR CRONOGRAMA (DTO)
    // =========================
    @Query("""
        SELECT new com.backend.comfutura.dto.response.CronogramaResponse(
            c.idCronograma,
            mp.descripcion,
            c.duracionDias,
            c.fechaInicio,
            c.fechaFin
        )
        FROM CronogramaOt c
        JOIN c.maestroPartida mp
        WHERE c.ots.idOts = :idOts
    """)
    List<CronogramaResponse> listarCronogramaPorOt(
            @Param("idOts") Integer idOts
    );

    // =========================
    // SUMAR DURACIÓN (DÍAS)
    // =========================
    @Query("""
        SELECT SUM(c.duracionDias)
        FROM CronogramaOt c
        WHERE c.ots.idOts = :idOts
    """)
    Optional<BigDecimal> sumarDuracionPorOt(
            @Param("idOts") Integer idOts
    );
}



