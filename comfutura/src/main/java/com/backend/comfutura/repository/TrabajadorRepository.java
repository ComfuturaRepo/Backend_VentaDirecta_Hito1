package com.backend.comfutura.repository;

import com.backend.comfutura.model.Trabajador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrabajadorRepository extends JpaRepository<Trabajador, Integer> {

    List<Trabajador> findByActivoTrueAndCargo_NombreOrderByApellidosAsc(String nombreCargo);

    Optional<Trabajador> findByDni(String dni);

    boolean existsByDni(String dni);

    boolean existsByDniAndIdTrabajadorNot(String dni, Integer id);

    Page<Trabajador> findByActivoTrue(Pageable pageable);

    // QUERY CORREGIDA - Usa los nombres correctos de las propiedades JPA
    @Query("SELECT t FROM Trabajador t WHERE " +
            "(:search IS NULL OR " +
            "LOWER(t.nombres) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(t.apellidos) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "t.dni LIKE CONCAT('%', :search, '%')) AND " +
            "(:activo IS NULL OR t.activo = :activo) AND " +
            "(:areaId IS NULL OR t.area.idArea = :areaId) AND " +
            "(:cargoId IS NULL OR t.cargo.id = :cargoId) AND " +
            "(:empresaId IS NULL OR (t.empresa IS NOT NULL AND t.empresa.id = :empresaId))")
    Page<Trabajador> searchTrabajadores(
            @Param("search") String search,
            @Param("activo") Boolean activo,
            @Param("areaId") Integer areaId,
            @Param("cargoId") Integer cargoId,
            @Param("empresaId") Integer empresaId,
            Pageable pageable);

    // Contar trabajadores activos por área
    @Query("SELECT COUNT(t) FROM Trabajador t WHERE t.area.idArea = :areaId AND t.activo = true")
    long countActivosByArea(@Param("areaId") Integer areaId);

    // Contar trabajadores por cargo
    @Query("SELECT COUNT(t) FROM Trabajador t WHERE t.cargo.id = :cargoId AND t.activo = true")
    long countByCargo(@Param("cargoId") Integer cargoId);


    //ESTO ES PARA EL DROPTOWN
    @Query("""
    SELECT t
    FROM Trabajador t
    WHERE t.activo = true
      AND LOWER(t.cargo.nombre) LIKE 'jefe%'
""")
    List<Trabajador> findActivosConCargoJefe();
    @Query("""
    SELECT t
    FROM Trabajador t
    WHERE t.activo = true
      AND LOWER(t.cargo.nombre) IN (
          'jefe de cierre',
          'jefe de cierre y liquidaciones'
      )
""")
    List<Trabajador> findJefesDeCierre();
    @Query("""
    SELECT t
    FROM Trabajador t
    WHERE t.activo = true
      AND LOWER(t.cargo.nombre) LIKE '%contabilidad%'
""")
    List<Trabajador> findActivosConCargoContabilidad();

    @Query("""
    SELECT t
    FROM Trabajador t
    WHERE t.activo = true
      AND LOWER(t.cargo.nombre) LIKE '%coordinador%'
""")
    List<Trabajador> findActivosConCargoCoordinador();

}

