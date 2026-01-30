package com.backend.comfutura.repository;

import com.backend.comfutura.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {

    Optional<Permiso> findByCodigo(String codigo);

    List<Permiso> findByActivoTrue();

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdPermisoNot(String codigo, Integer idPermiso);

    @Query("SELECT p FROM Permiso p JOIN p.niveles n WHERE n.idNivel = :idNivel AND p.activo = true")
    List<Permiso> findByNivelId(@Param("idNivel") Integer idNivel);

    @Query("SELECT p FROM Permiso p JOIN p.areas a WHERE a.idArea = :idArea AND p.activo = true")
    List<Permiso> findByAreaId(@Param("idArea") Integer idArea);

    @Query("SELECT p FROM Permiso p JOIN p.cargos c WHERE c.idCargo = :idCargo AND p.activo = true")
    List<Permiso> findByCargoId(@Param("idCargo") Integer idCargo);

    @Query("SELECT p FROM Permiso p WHERE p.codigo = :codigoPermiso AND p.activo = true AND " +
            "((:idNivel IS NOT NULL AND EXISTS (SELECT 1 FROM p.niveles n WHERE n.idNivel = :idNivel)) OR " +
            "(:idArea IS NOT NULL AND EXISTS (SELECT 1 FROM p.areas a WHERE a.idArea = :idArea)) OR " +
            "(:idCargo IS NOT NULL AND EXISTS (SELECT 1 FROM p.cargos c WHERE c.idCargo = :idCargo)))")
    Optional<Permiso> findPermisoByUsuario(
            @Param("codigoPermiso") String codigoPermiso,
            @Param("idNivel") Integer idNivel,
            @Param("idArea") Integer idArea,
            @Param("idCargo") Integer idCargo
    );
    @Query("SELECT DISTINCT p FROM Permiso p WHERE p.activo = true AND " +
            "(EXISTS (SELECT 1 FROM p.niveles n WHERE n.idNivel = :idNivel) OR " +
            "EXISTS (SELECT 1 FROM p.areas a WHERE a.idArea = :idArea) OR " +
            "EXISTS (SELECT 1 FROM p.cargos c WHERE c.idCargo = :idCargo))")
    List<Permiso> findPermisosByUsuario(
            @Param("idNivel") Integer idNivel,
            @Param("idArea") Integer idArea,
            @Param("idCargo") Integer idCargo
    );
}