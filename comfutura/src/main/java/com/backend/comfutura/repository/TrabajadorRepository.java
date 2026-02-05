package com.backend.comfutura.repository;

import com.backend.comfutura.model.Trabajador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer>, JpaSpecificationExecutor<Trabajador> {

    // Métodos básicos
    Optional<Trabajador> findByDni(String dni);
    Optional<Trabajador> findByCorreoCorporativo(String correoCorporativo);
    boolean existsByDni(String dni);
    boolean existsByCorreoCorporativo(String correoCorporativo);
    boolean existsByDniAndIdTrabajadorNot(String dni, Integer id);
    boolean existsByCorreoCorporativoAndIdTrabajadorNot(String correoCorporativo, Integer id);

    // Listados paginados
    Page<Trabajador> findByActivoTrue(Pageable pageable);
    List<Trabajador> findByActivoTrueOrderByNombresAsc();
    List<Trabajador> findAllByActivoTrueOrderByApellidosAsc();

    // Trabajadores sin usuario
    @Query("""
        SELECT t
        FROM Trabajador t
        LEFT JOIN Usuario u ON u.trabajador = t
        WHERE u.idUsuario IS NULL
          AND t.activo = true
        ORDER BY t.apellidos, t.nombres
    """)
    List<Trabajador> findTrabajadoresActivosSinUsuario();

    // Filtros por cargo específico
    List<Trabajador> findByActivoTrueAndCargo_NombreOrderByApellidosAsc(String nombreCargo);

    // Consulta corregida con los nombres correctos de campos
    @Query("""
        SELECT t FROM Trabajador t
        WHERE (:search IS NULL OR
               LOWER(t.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(t.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR
               t.dni LIKE CONCAT('%', :search, '%'))
          AND (:activo IS NULL OR t.activo = :activo)
          AND (:areaId IS NULL OR t.area.idArea = :areaId)
          AND (:cargoId IS NULL OR t.cargo.idCargo = :cargoId)
          AND (:empresaId IS NULL OR (t.empresa IS NOT NULL AND t.empresa.id = :empresaId))
    """)
    Page<Trabajador> searchTrabajadoresSimple(
            @Param("search") String search,
            @Param("activo") Boolean activo,
            @Param("areaId") Integer areaId,
            @Param("cargoId") Integer cargoId,
            @Param("empresaId") Integer empresaId,
            Pageable pageable);

    // Conteos
    @Query("SELECT COUNT(t) FROM Trabajador t WHERE t.area.idArea = :areaId AND t.activo = true")
    long countActivosByArea(@Param("areaId") Integer areaId);

    @Query("SELECT COUNT(t) FROM Trabajador t WHERE t.cargo.idCargo = :cargoId")
    long countByCargo(@Param("cargoId") Integer cargoId);

    // Métodos nuevos para filtrar por campos booleanos (roles)
    List<Trabajador> findAllByActivoTrueAndPuedeSerCoordinadorTiCwTrueOrderByApellidosAsc();
    List<Trabajador> findAllByActivoTrueAndPuedeSerJefaturaResponsableTrueOrderByApellidosAsc();
    List<Trabajador> findAllByActivoTrueAndPuedeSerLiquidadorTrueOrderByApellidosAsc();
    List<Trabajador> findAllByActivoTrueAndPuedeSerEjecutanteTrueOrderByApellidosAsc();
    List<Trabajador> findAllByActivoTrueAndPuedeSerAnalistaContableTrueOrderByApellidosAsc();

    // Analista Contable
    @Query("""
        SELECT t FROM Trabajador t
        WHERE t.activo = true
          AND LOWER(t.cargo.nombre) LIKE '%contabilidad%'
        ORDER BY t.apellidos, t.nombres
    """)
    List<Trabajador> findActivosConCargoContabilidad();

    // Coordinador TI CW
    @Query("""
        SELECT t FROM Trabajador t
        WHERE t.activo = true
          AND (LOWER(t.cargo.nombre) LIKE '%coordinador%' 
            OR LOWER(t.cargo.nombre) LIKE '%gerente%'
            OR LOWER(t.cargo.nombre) LIKE '%jefe%'
            OR LOWER(t.cargo.nombre) LIKE '%project manager%'
            OR LOWER(t.cargo.nombre) LIKE '%coordinador cw%')
        ORDER BY t.apellidos, t.nombres
    """)
    List<Trabajador> findActivosConCargoCoordinador();

    // Método para búsqueda avanzada con Specifications (recomendado)
    Page<Trabajador> findAll(Specification<Trabajador> spec, Pageable pageable);


}