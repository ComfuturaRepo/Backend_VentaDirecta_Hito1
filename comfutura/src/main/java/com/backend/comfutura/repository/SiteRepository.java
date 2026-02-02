package com.backend.comfutura.repository;

import com.backend.comfutura.model.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Integer> {

    List<Site> findByActivoTrueOrderByCodigoSitioAsc();

    Page<Site> findAll(Specification<Site> spec, Pageable pageable);

    Page<Site> findByActivoTrue(Pageable pageable);

    boolean existsByCodigoSitio(String codigoSitio);

    boolean existsByCodigoSitioAndIdSiteNot(String codigoSitio, Integer id);

    @Query("SELECT DISTINCT s FROM Site s LEFT JOIN FETCH s.descripciones d WHERE s.activo = true AND d.activo = true")
    Page<Site> findAllActivosWithDescripciones(Pageable pageable);

    @Query("SELECT s FROM Site s WHERE s.idSite = :id AND s.activo = true")
    Site findActivoById(@Param("id") Integer id);
}