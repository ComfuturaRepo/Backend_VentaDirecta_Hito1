package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.inspeccion.InspeccionHerramienta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InspeccionHerramientaRepository extends JpaRepository<InspeccionHerramienta, Integer> {

    @Query("SELECT i FROM InspeccionHerramienta i LEFT JOIN FETCH i.supervisor")
    List<InspeccionHerramienta> findAllWithSupervisor();

    List<InspeccionHerramienta> findByNumeroRegistro(String numeroRegistro);
}