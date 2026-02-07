package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaChecklistFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaChecklistFotoRepository extends JpaRepository<SsomaChecklistFoto, Integer> {

    // Usado en: convertirChecklistAResponse()
    List<SsomaChecklistFoto> findByChecklistSeguridadIdChecklist(Integer idChecklist);
}