package com.backend.comfutura.service;



import com.backend.comfutura.model.OrdenCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrdenCompraService {

    OrdenCompra crear(OrdenCompra ordenCompra);

    OrdenCompra actualizar(Integer id, OrdenCompra ordenCompra);

    Optional<OrdenCompra> obtenerPorId(Integer id);

    Page<OrdenCompra> listar(Pageable pageable);

    void eliminar(Integer id);

}
