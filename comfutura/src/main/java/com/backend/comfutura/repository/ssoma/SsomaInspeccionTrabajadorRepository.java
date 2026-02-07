package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaInspeccionTrabajador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaInspeccionTrabajadorRepository extends JpaRepository<SsomaInspeccionTrabajador, Integer> {

    // Usado en: convertirAResponseDTO()
    List<SsomaInspeccionTrabajador> findBySsomaFormularioIdSsoma(Integer idSsoma);
}