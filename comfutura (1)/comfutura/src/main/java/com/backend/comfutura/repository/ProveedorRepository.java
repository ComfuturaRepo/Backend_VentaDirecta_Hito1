package com.backend.comfutura.repository;

import com.backend.comfutura.model.Proveedor;
import com.backend.comfutura.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor,Integer> {

    List<Proveedor> findByActivoTrueOrderByRazonSocialAsc();

}
