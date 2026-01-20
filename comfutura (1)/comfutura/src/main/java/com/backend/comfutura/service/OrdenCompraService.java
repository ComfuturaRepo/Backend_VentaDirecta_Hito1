package com.backend.comfutura.service;



import com.backend.comfutura.model.OrdenCompra;

import java.util.List;
import java.util.Optional;

public interface OrdenCompraService {

    OrdenCompra crear(OrdenCompra ordenCompra);

    OrdenCompra actualizar(Integer id, OrdenCompra ordenCompra);

    Optional<OrdenCompra> obtenerPorId(Integer id);

    List<OrdenCompra> listar();

    void eliminar(Integer id);

}
