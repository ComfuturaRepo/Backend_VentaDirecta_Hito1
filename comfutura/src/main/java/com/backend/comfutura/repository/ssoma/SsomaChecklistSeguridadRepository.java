package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaChecklistSeguridad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaChecklistSeguridadRepository extends JpaRepository<SsomaChecklistSeguridad, Integer> {

    // Usado en: convertirAResponseDTO()
    List<SsomaChecklistSeguridad> findBySsomaFormularioIdSsoma(Integer idSsoma);
}