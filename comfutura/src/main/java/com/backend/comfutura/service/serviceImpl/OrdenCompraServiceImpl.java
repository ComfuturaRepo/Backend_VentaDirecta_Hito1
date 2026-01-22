        package com.backend.comfutura.service.serviceImpl;



import com.backend.comfutura.model.OrdenCompra;
import com.backend.comfutura.repository.OrdenCompraRepository;
import com.backend.comfutura.service.OrdenCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;

    @Override
    public OrdenCompra crear(OrdenCompra ordenCompra) {
        return ordenCompraRepository.save(ordenCompra);
    }

    @Override
    public OrdenCompra actualizar(Integer id, OrdenCompra ordenCompra) {
        OrdenCompra existente = ordenCompraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));

        existente.setEstadoOc(ordenCompra.getEstadoOc());
        existente.setOts(ordenCompra.getOts());
        existente.setMaestro(ordenCompra.getMaestro());
        existente.setProveedor(ordenCompra.getProveedor());
        existente.setCantidad(ordenCompra.getCantidad());
        existente.setCostoUnitario(ordenCompra.getCostoUnitario());
        existente.setObservacion(ordenCompra.getObservacion());

        return ordenCompraRepository.save(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenCompra> obtenerPorId(Integer id) {
        return ordenCompraRepository.findById(id);
    }


    public Page<OrdenCompra> listar(Pageable pageable) {
        return ordenCompraRepository.findAll(pageable);
    }

    @Override
    public void eliminar(Integer id) {
        if (!ordenCompraRepository.existsById(id)) {
            throw new RuntimeException("Orden de compra no encontrada");
        }
        ordenCompraRepository.deleteById(id);
    }
}