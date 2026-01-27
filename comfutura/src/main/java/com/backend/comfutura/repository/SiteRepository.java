package com.backend.comfutura.repository;

import com.backend.comfutura.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Integer> {

    List<Site> findByActivoTrueOrderByCodigoSitioAsc();
    Optional<Site> findByCodigoSitio(String codigoSitio);

}