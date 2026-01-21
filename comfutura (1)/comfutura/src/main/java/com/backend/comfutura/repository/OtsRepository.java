package com.backend.comfutura.repository;

import com.backend.comfutura.model.Ots;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtsRepository extends JpaRepository<Ots, Integer> {

    /**
     * Verifica si ya existe una OT con el número OT indicado
     */
    boolean existsByOt(Integer ot);

    /**
     * Busca una OT por su número OT visible (el campo 'ot')
     * Retorna Optional porque puede no existir
     */
    Optional<Ots> findByOt(Integer ot);

    /**
     * Busca OTs por estado activo/inactivo con paginación
     */
    Page<Ots> findByActivo(Boolean activo, Pageable pageable);

    /**
     * Lista todas las OTs activas ordenadas por número OT ascendente
     * (útil para combos o listas sin paginación)
     */
    List<Ots> findByActivoTrueOrderByOtAsc();

    /**
     * Obtiene la OT con el número OT más alto
     * Usado para generar el siguiente número secuencial
     */
    Optional<Ots> findTopByOrderByOtDesc();

    // Opcional: si necesitas buscar por otros campos frecuentes
    // Optional<Ots> findByDescripcionContainingIgnoreCase(String texto);
    // Page<Ots> findByClienteIdAndActivo(Integer idCliente, Boolean activo, Pageable pageable);
}