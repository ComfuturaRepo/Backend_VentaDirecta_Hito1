package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaFormulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface SsomaFormularioRepository extends JpaRepository<SsomaFormulario, Integer> {

    // Usado en: obtenerFormulariosPorOt()
    List<SsomaFormulario> findByOtsIdOts(Integer idOts);
}