package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.SsomaCharlaVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SsomaCharlaVideoRepository extends JpaRepository<SsomaCharlaVideo, Integer> {

    // Usado en: convertirCharlaAResponse()
    Optional<SsomaCharlaVideo> findByCharlaIdCharla(Integer idCharla);
}