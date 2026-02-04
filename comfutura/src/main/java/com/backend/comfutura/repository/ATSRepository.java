package com.backend.comfutura.repository;


import com.backend.comfutura.model.ssoma.ATS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ATSRepository extends JpaRepository<ATS, Integer> {

    @Query("SELECT a FROM ATS a ORDER BY a.fecha DESC, a.hora DESC")
    Page<ATS> findAllOrderByFechaDesc(Pageable pageable);

    @Query("SELECT a FROM ATS a WHERE a.empresa LIKE %:search% OR a.lugarTrabajo LIKE %:search% ORDER BY a.fecha DESC")
    Page<ATS> search(String search, Pageable pageable);
}