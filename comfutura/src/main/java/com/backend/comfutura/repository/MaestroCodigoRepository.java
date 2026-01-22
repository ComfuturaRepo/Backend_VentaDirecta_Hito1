package com.backend.comfutura.repository;

import com.backend.comfutura.model.MaestroCodigo;
import com.backend.comfutura.model.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaestroCodigoRepository extends JpaRepository<MaestroCodigo,Integer> {
    List<MaestroCodigo> findByActivoTrueOrderByCodigoAsc();

}
