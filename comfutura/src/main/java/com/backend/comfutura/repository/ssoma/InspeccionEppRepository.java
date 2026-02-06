package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.capacitacion.Capacitacion;
import com.backend.comfutura.model.ssoma.inspeccion.InspeccionEpp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InspeccionEppRepository extends JpaRepository<InspeccionEpp, Integer> {
    List<InspeccionEpp> findByOtsIdOts(Integer idOts); // Agrega este método

}