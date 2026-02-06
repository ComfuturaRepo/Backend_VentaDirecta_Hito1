package com.backend.comfutura.repository;

import com.backend.comfutura.model.MaestroMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaestroMaterialRepository extends JpaRepository<MaestroMaterial, Integer> {

    // Paginación
    Page<MaestroMaterial> findByActivoTrueOrderByCodigoAsc(Pageable pageable);

    // Para dropdown
    List<MaestroMaterial> findByActivoTrueOrderByCodigoAsc();
}