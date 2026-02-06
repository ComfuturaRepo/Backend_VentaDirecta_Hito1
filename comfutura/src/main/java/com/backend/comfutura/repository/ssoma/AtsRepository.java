package com.backend.comfutura.repository.ssoma;

import com.backend.comfutura.model.ssoma.ats.Ats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AtsRepository extends JpaRepository<Ats, Integer> {
    List<Ats> findByOtsIdOts(Integer idOts);
}