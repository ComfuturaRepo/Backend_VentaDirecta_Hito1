package com.backend.comfutura.repository;

import com.backend.comfutura.model.Site;
import com.backend.comfutura.model.SiteDescripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.util.Collection;
import java.util.List;

@Repository
public interface SiteDescripcionRepository extends JpaRepository<SiteDescripcion, Integer> {

    List<SiteDescripcion> findByActivoTrueOrderBySite_CodigoSitioAscDescripcionAsc();


    List<SiteDescripcion> findBySiteIdSiteAndActivoTrue(Integer idSite);

    // Nuevos métodos para las búsquedas
    List<SiteDescripcion> findBySiteCodigoSitioIgnoreCaseAndActivoTrue(String codigoSitio);

    // Buscar descripciones donde el site tenga código nulo o vacío
    @Query("SELECT sd FROM SiteDescripcion sd WHERE " +
            "(sd.site.codigoSitio IS NULL OR sd.site.codigoSitio = '') " +
            "AND sd.activo = true")
    List<SiteDescripcion> findBySiteCodigoSitioIsNullOrSiteCodigoSitioEmpty();

    // Buscar descripciones por site específico
    List<SiteDescripcion> findBySiteAndActivoTrue(Site site);

    // Buscar descripciones por id de site
    @Modifying
    @Query("UPDATE SiteDescripcion d SET d.activo = false WHERE d.site.idSite = :idSite")
    void deactivateAllBySiteId(@Param("idSite") Integer idSite);

}
