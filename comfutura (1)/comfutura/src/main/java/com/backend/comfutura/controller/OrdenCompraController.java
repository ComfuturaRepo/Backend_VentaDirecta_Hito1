package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.OrdenCompraRequestDTO;
import com.backend.comfutura.dto.response.OrdenCompraResponseDTO;
import com.backend.comfutura.model.*;
import com.backend.comfutura.service.OrdenCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/ordenes-compra")
@RequiredArgsConstructor
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;

    @PostMapping
    public OrdenCompraResponseDTO crear(@RequestBody OrdenCompraRequestDTO dto) {

        // Relaciones
        EstadoOc estado = new EstadoOc();
        estado.setIdEstadoOc(Integer.parseInt(dto.getIdEstadoOc()));

        Ots ots = new Ots();
        ots.setIdOts(Integer.parseInt(dto.getIdOts()));

        MaestroCodigo maestro = new MaestroCodigo();
        maestro.setId(Integer.parseInt(dto.getIdMaestro())); // campo real de tu entidad

        Proveedor proveedor = new Proveedor();
        proveedor.setId(Integer.parseInt(dto.getIdProveedor())); // campo real

        // Orden de compra
        OrdenCompra oc = new OrdenCompra();
        oc.setEstadoOc(estado);
        oc.setOts(ots);
        oc.setMaestro(maestro);
        oc.setProveedor(proveedor);
        oc.setCantidad(new BigDecimal(dto.getCantidad()));
        oc.setCostoUnitario(new BigDecimal(dto.getCostoUnitario()));
        oc.setObservacion(dto.getObservacion());

        // Guardar
        OrdenCompra guardada = ordenCompraService.crear(oc);

        // DTO de respuesta
        OrdenCompraResponseDTO response = new OrdenCompraResponseDTO();
        response.setIdOc(String.valueOf(guardada.getIdOc()));
        response.setEstado(String.valueOf(guardada.getEstadoOc().getIdEstadoOc()));
        response.setOts(String.valueOf(guardada.getOts().getIdOts()));
        response.setMaestro(String.valueOf(guardada.getMaestro().getId()));
        response.setProveedor(guardada.getProveedor().getRazonSocial()); // tu campo real
        response.setCantidad(guardada.getCantidad().toPlainString());
        response.setCostoUnitario(guardada.getCostoUnitario().toPlainString());
        response.setFechaOc(guardada.getFechaOc().toString());
        response.setObservacion(guardada.getObservacion());

        return response;
    }
}
