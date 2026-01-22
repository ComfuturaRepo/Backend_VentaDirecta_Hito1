package com.backend.comfutura.service;

import com.backend.comfutura.model.Site;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SiteService {

    Site guardar(Site site);

    Page<Site> listar(Pageable pageable);

    void toggle(Integer id);
}
