package com.backend.comfutura.repository;

import com.backend.comfutura.model.SiteDescripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.*;

import java.util.List;

@Repository
public interface SiteDescripcionRepository extends JpaRepository<SiteDescripcion, Integer> {

    List<SiteDescripcion> findByActivoTrueOrderBySite_CodigoSitioAscDescripcionAsc();


    List<SiteDescripcion> findBySiteIdSiteAndActivoTrue(Integer idSite);

    @Modifying
    @Query("UPDATE SiteDescripcion d SET d.activo = false WHERE d.site.idSite = :idSite")
    void deactivateAllBySiteId(@Param("idSite") Integer idSite);
}
