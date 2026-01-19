package com.backend.comfutura.repository;

import com.backend.comfutura.model.Ots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtsRepository extends JpaRepository<Ots,Integer > {
    boolean existsByOt(Integer ot);
}
