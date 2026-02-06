package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.inspeccion.InspeccionEpp;
import com.backend.comfutura.model.ssoma.inspeccion.InspeccionHerramienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InspeccionHerramientaRepository extends JpaRepository<InspeccionHerramienta, Integer> {
    List<InspeccionHerramienta> findByOtsIdOts(Integer idOts); // Agrega este método

}