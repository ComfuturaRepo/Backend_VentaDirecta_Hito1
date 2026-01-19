package com.backend.comfutura.repository;

import com.backend.comfutura.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Integer> {
    List<Cliente> findByActivoTrueOrderByRazonSocialAsc();
}
