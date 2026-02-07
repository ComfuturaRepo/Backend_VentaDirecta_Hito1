package com.backend.comfutura.repository.ssoma;


import com.backend.comfutura.model.ssoma.SsomaPetar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SsomaPetarRepository extends JpaRepository<SsomaPetar, Integer> {

    // Usado en: convertirAResponseDTO()
    Optional<SsomaPetar> findBySsomaFormularioIdSsoma(Integer idSsoma);
}