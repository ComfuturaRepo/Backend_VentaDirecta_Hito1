package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.catalogo.TipoRiesgoTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoRiesgoTrabajoRepository extends JpaRepository<TipoRiesgoTrabajo, Integer> {
    List<TipoRiesgoTrabajo> findByActivoTrueOrderByNombreAsc();
}