package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaParticipanteFirma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SsomaParticipanteFirmaRepository extends JpaRepository<SsomaParticipanteFirma, Integer> {

    // Usado en: convertirParticipantesAResponse()
    Optional<SsomaParticipanteFirma> findByParticipanteIdParticipante(Integer idParticipante);
}