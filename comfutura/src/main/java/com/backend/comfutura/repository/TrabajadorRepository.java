package com.backend.comfutura.repository;

import com.backend.comfutura.model.Trabajador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {

    // Métodos básicos
    Optional<Trabajador> findByDni(String dni);
    boolean existsByDni(String dni);
    boolean existsByDniAndIdTrabajadorNot(String dni, Integer id);

    // Listados generales
    List<Trabajador> findByActivoTrueAndCargo_NombreOrderByApellidosAsc(String nombreCargo);
    List<Trabajador> findAllByActivoTrueOrderByApellidosAsc();

    Page<Trabajador> findByActivoTrue(Pageable pageable);

    // Búsqueda avanzada
    @Query("""
        SELECT t FROM Trabajador t
        WHERE (:search IS NULL OR
               LOWER(t.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(t.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR
               t.dni LIKE CONCAT('%', :search, '%'))
          AND (:activo IS NULL OR t.activo = :activo)
          AND (:areaId IS NULL OR t.area.idArea = :areaId)
          AND (:cargoId IS NULL OR t.cargo.id = :cargoId)
          AND (:empresaId IS NULL OR (t.empresa IS NOT NULL AND t.empresa.id = :empresaId))
        """)
    Page<Trabajador> searchTrabajadores(
            @Param("search") String search,
            @Param("activo") Boolean activo,
            @Param("areaId") Integer areaId,
            @Param("cargoId") Integer cargoId,
            @Param("empresaId") Integer empresaId,
            Pageable pageable);

    // Conteos
    @Query("SELECT COUNT(t) FROM Trabajador t WHERE t.area.idArea = :areaId AND t.activo = true")
    long countActivosByArea(@Param("areaId") Integer areaId);

    @Query("SELECT COUNT(t) FROM Trabajador t WHERE t.cargo.id = :cargoId AND t.activo = true")
    long countByCargo(@Param("cargoId") Integer cargoId);

    // ────────────────────────────────────────────────
    // Consultas específicas para roles en OT
    // ────────────────────────────────────────────────

    // Jefatura Responsable → cargos que contengan "jefe" o "jefa"
    @Query("""
        SELECT t FROM Trabajador t
        WHERE t.activo = true
          AND (LOWER(t.cargo.nombre) LIKE '%jefe%' OR LOWER(t.cargo.nombre) LIKE '%jefa%')
        ORDER BY t.apellidos, t.nombres
        """)
    List<Trabajador> findActivosConCargoJefe();

    // Liquidador → solo cargos específicos
    @Query("""
        SELECT t FROM Trabajador t
        WHERE t.activo = true
          AND LOWER(t.cargo.nombre) IN ('jefe de cierre', 'jefe de cierre y liquidaciones')
        ORDER BY t.apellidos, t.nombres
        """)
    List<Trabajador> findJefesDeCierre();

    // Analista Contable → cargos con "contabilidad"
    @Query("""
        SELECT t FROM Trabajador t
        WHERE t.activo = true
          AND LOWER(t.cargo.nombre) LIKE '%contabilidad%'
        ORDER BY t.apellidos, t.nombres
        """)
    List<Trabajador> findActivosConCargoContabilidad();

    // Coordinador TI CW → más selectivo (evitar traer todos los jefes)
    @Query("""
        SELECT t FROM Trabajador t
        WHERE t.activo = true
          AND (LOWER(t.cargo.nombre) LIKE '%coordinador%' 
            OR LOWER(t.cargo.nombre) LIKE '%GERENTE%'
            OR LOWER(t.cargo.nombre) LIKE '%JEFE%'
            OR LOWER(t.cargo.nombre) LIKE '%project manager%'
            OR LOWER(t.cargo.nombre) LIKE '%coordinador cw%')
        ORDER BY t.apellidos, t.nombres
        """)
    List<Trabajador> findActivosConCargoCoordinador();

    Page<Trabajador> findAll(Specification<Trabajador> spec, Pageable pageable);
}