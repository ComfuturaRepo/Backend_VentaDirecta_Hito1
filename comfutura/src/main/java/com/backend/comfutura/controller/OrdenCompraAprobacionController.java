package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.OrdenCompraAprobacionRequest;
import com.backend.comfutura.dto.response.OrdenCompraAprobacionResponse;
import com.backend.comfutura.service.OrdenCompraAprobacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orden-compra-aprobaciones")
public class OrdenCompraAprobacionController {

    @Autowired
    private OrdenCompraAprobacionService service;

    @PostMapping("/{idOc}/aprobar")
    public ResponseEntity<OrdenCompraAprobacionResponse> aprobar(
            @PathVariable Integer idOc,
            @RequestBody OrdenCompraAprobacionRequest request
    ) {
        return ResponseEntity.ok(
                service.aprobar(
                        idOc,
                        request.getNivel(),
                        request.getEstado(),
                        request
                )
        );
    }

    @GetMapping("/{idOc}")
    public ResponseEntity<List<OrdenCompraAprobacionResponse>> historial(
            @PathVariable Integer idOc
    ) {
        return ResponseEntity.ok(service.obtenerHistorial(idOc));
    }


}
