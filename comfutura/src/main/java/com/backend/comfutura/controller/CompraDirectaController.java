package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.CompraDirectaCreateRequest;
import com.backend.comfutura.dto.response.CompraDirectaResponse;
import com.backend.comfutura.service.CompraDirectaService;
import com.backend.comfutura.dto.response.CompraDirectaDetalleResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compra-directa")
@RequiredArgsConstructor
public class CompraDirectaController {

    private final CompraDirectaService service;

    // =========================
    // CREAR
    // =========================
    @PostMapping
    public CompraDirectaResponse crear(
            @RequestBody CompraDirectaCreateRequest request
    ) {
        return service.crear(request);
    }

    // =========================
    // LISTAR (GRILLA)
    // =========================
    @GetMapping
    public Page<CompraDirectaResponse> listar(
            @RequestParam(defaultValue = "0") int page
    ) {
        return service.listar(page);
    }

    // =========================
    // DETALLE POR OT (UI)
    // =========================
    @GetMapping("/ot/{ot}") // list compra directa , tiene que volver la lista
    public List<CompraDirectaResponse> obtenerPorOt(@PathVariable Integer ot) {
        return service.obtenerPorOt(ot);
    }

    // =========================
    // CAMBIAR ESTADO (BACKEND)
    // =========================
    @PatchMapping("/{id}/estado")
    public void cambiarEstado(
            @PathVariable Integer id,
            @RequestParam String estado,
            @RequestParam(required = false) String observacion
    ) {
        service.cambiarEstado(id, estado, observacion);
    }
    // =========================


}

