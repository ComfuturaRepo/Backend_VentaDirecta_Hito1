package com.backend.comfutura.repository;

import com.backend.comfutura.model.Cliente;
import com.backend.comfutura.model.Ots;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Integer> {
    List<Cliente> findByActivoTrueOrderByRazonSocialAsc();

    Optional<Cliente> findByRuc(String ruc);

    boolean existsByRuc(String ruc);

    boolean existsByRucAndIdClienteNot(String ruc, Integer idCliente);

    Page<Cliente> findByActivoTrue(Pageable pageable);

    Page<Cliente> findByActivoFalse(Pageable pageable);

    Page<Cliente> findAll(Pageable pageable);

    @Query("SELECT c FROM Cliente c WHERE LOWER(c.razonSocial) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(c.ruc) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Cliente> search(@Param("search") String search, Pageable pageable);

    @Query("SELECT c FROM Cliente c JOIN c.areas a WHERE a.idArea = :areaId")
    List<Cliente> findByAreaId(@Param("areaId") Integer areaId);
}
