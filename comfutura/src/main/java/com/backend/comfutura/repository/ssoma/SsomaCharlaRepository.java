package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaCharla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SsomaCharlaRepository extends JpaRepository<SsomaCharla, Integer> {

    // Usado en: convertirAResponseDTO()
    Optional<SsomaCharla> findBySsomaFormularioIdSsoma(Integer idSsoma);
}