package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.ats.Ats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AtsRepository extends JpaRepository<Ats, Integer> {

    @Query("SELECT a FROM Ats a LEFT JOIN FETCH a.trabajo")
    List<Ats> findAllWithTrabajo();

    @Query("SELECT a FROM Ats a WHERE a.fecha = :fecha")
    List<Ats> findByFecha(@Param("fecha") java.time.LocalDate fecha);
}