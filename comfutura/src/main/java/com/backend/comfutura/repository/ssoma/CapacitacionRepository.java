package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.capacitacion.Capacitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CapacitacionRepository extends JpaRepository<Capacitacion, Integer> {

    @Query("SELECT c FROM Capacitacion c LEFT JOIN FETCH c.capacitador")
    List<Capacitacion> findAllWithCapacitador();

    List<Capacitacion> findByNumeroRegistroContaining(String numeroRegistro);
}