package com.backend.comfutura.repository.ssoma;


import com.backend.comfutura.model.ssoma.SsomaHerramientaFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaHerramientaFotoRepository extends JpaRepository<SsomaHerramientaFoto, Integer> {

    // Usado en: convertirHerramientasAResponse()
    List<SsomaHerramientaFoto> findByHerramientaInspeccionIdHerramienta(Integer idHerramienta);
}