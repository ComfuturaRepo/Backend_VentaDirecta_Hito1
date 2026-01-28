package com.backend.comfutura.repository;

import com.backend.comfutura.model.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Integer> {

    List<Site> findByActivoTrueOrderByCodigoSitioAsc();
    Page<Site> findByActivoTrue(Pageable safePageable);

    boolean existsByCodigoSitioAndIdSiteNot(String codigoSitio, Integer id);

    boolean existsByCodigoSitio(String codigoSitio);



    Page<Site> findAll(Specification<Site> spec, Pageable pageable);
}