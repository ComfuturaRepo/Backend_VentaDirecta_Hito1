package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.inspeccion.InspeccionHerramienta;
import com.backend.comfutura.model.ssoma.petar.Petar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PetarRepository extends JpaRepository<Petar, Integer> {
    List<Petar> findByOtsIdOts(Integer idOts); // Agrega este método

}