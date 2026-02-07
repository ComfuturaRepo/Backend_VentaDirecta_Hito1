package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaSecuenciaTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaSecuenciaTareaRepository extends JpaRepository<SsomaSecuenciaTarea, Integer> {

    // Usado en: convertirAResponseDTO()
    List<SsomaSecuenciaTarea> findBySsomaFormularioIdSsoma(Integer idSsoma);
}