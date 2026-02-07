package com.backend.comfutura.repository.ssoma;


import com.backend.comfutura.model.ssoma.SsomaEppCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaEppCheckRepository extends JpaRepository<SsomaEppCheck, Integer> {

    // Usado en: convertirAResponseDTO()
    List<SsomaEppCheck> findBySsomaFormularioIdSsoma(Integer idSsoma);
}