package com.backend.comfutura.repository.ssoma;



import com.backend.comfutura.model.ssoma.SsomaEppFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsomaEppFotoRepository extends JpaRepository<SsomaEppFoto, Integer> {

    // Usado en: convertirEppChecksAResponse()
    List<SsomaEppFoto> findByEppCheckIdEpp(Integer idEpp);
}