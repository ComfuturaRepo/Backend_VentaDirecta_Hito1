package com.backend.comfutura.repository.ssoma;


import com.backend.comfutura.model.ssoma.SsomaParticipanteFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaParticipanteFotoRepository extends JpaRepository<SsomaParticipanteFoto, Integer> {

    // Usado en: convertirParticipantesAResponse()
    List<SsomaParticipanteFoto> findByParticipanteIdParticipante(Integer idParticipante);
}