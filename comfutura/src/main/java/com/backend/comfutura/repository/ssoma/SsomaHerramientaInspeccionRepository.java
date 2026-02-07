package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaHerramientaInspeccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaHerramientaInspeccionRepository extends JpaRepository<SsomaHerramientaInspeccion, Integer> {

    // Usado en: convertirAResponseDTO()
    List<SsomaHerramientaInspeccion> findBySsomaFormularioIdSsoma(Integer idSsoma);
}