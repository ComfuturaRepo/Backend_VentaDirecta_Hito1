package com.backend.comfutura.repository;

import com.backend.comfutura.model.Aprobador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AprobadorRepository extends JpaRepository<Aprobador, Long> {

    List<Aprobador> findByCliente_IdClienteAndArea_IdAreaAndNivelAndActivoTrue(
            Integer idCliente,
            Integer idArea,
            Integer nivel
    );
}
