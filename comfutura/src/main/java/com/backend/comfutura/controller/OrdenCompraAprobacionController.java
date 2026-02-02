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

    /**
     * Aprobar / Rechazar una OC por nivel
     */
    @PostMapping("/{idOc}/{nivel}")
    public ResponseEntity<OrdenCompraAprobacionResponse> aprobar(
            @PathVariable Integer idOc,
            @PathVariable Integer nivel,
            @RequestParam String estado,
            @RequestBody OrdenCompraAprobacionRequest request) {

        return ResponseEntity.ok(
                service.aprobar(idOc, nivel, estado, request)
        );
    }

    /**
     * Obtener historial de aprobaciones
     */
    @GetMapping("/{idOc}")
    public ResponseEntity<List<OrdenCompraAprobacionResponse>> historial(
            @PathVariable Integer idOc) {

        return ResponseEntity.ok(
                service.obtenerHistorial(idOc)
        );
    }
}

