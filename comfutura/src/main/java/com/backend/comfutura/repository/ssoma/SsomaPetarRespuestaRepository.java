package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaPetarRespuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaPetarRespuestaRepository extends JpaRepository<SsomaPetarRespuesta, Integer> {

    // Usado en: convertirPetarAResponse()
    List<SsomaPetarRespuesta> findByPetarIdPetar(Integer idPetar);
}