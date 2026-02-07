package com.backend.comfutura.repository.ssoma;


import com.backend.comfutura.model.ssoma.SsomaParticipante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaParticipanteRepository extends JpaRepository<SsomaParticipante, Integer> {

    // Usado en: convertirAResponseDTO()
    List<SsomaParticipante> findBySsomaFormularioIdSsoma(Integer idSsoma);
}