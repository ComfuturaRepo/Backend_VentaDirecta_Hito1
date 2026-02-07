package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaEquipoProteccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaEquipoProteccionRepository extends JpaRepository<SsomaEquipoProteccion, Integer> {

    // Usado en: convertirPetarAResponse()
    List<SsomaEquipoProteccion> findByPetarIdPetar(Integer idPetar);
}