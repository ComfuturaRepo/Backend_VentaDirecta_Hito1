package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.ats.Ats;
import com.backend.comfutura.model.ssoma.capacitacion.Capacitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CapacitacionRepository extends JpaRepository<Capacitacion, Integer> {
    List<Capacitacion> findByOtsIdOts(Integer idOts); // Agrega este método

}