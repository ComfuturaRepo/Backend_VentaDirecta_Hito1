package com.backend.comfutura.repository;


import com.backend.comfutura.model.MaestroPartida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;


public interface MaestroPartidaRepository extends JpaRepository<MaestroPartida, Integer> {


    // Paginación
    Page<MaestroPartida> findByActivoTrueOrderByCodigoAsc(Pageable pageable);


    // Para dropdown
    List<MaestroPartida> findByActivoTrueOrderByCodigoAsc();
}