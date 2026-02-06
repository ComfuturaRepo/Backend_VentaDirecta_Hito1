package com.backend.comfutura.controller;

import com.backend.comfutura.dto.response.MaestroPartidaResponse;
import com.backend.comfutura.service.MaestroPartidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maestro-partidas")
@RequiredArgsConstructor
public class MaestroPartidaController {

    private final MaestroPartidaService service;

    // ==========================
    // LISTAR (paginado)
    // ==========================
    @GetMapping
    public Page<MaestroPartidaResponse> listar(
            @RequestParam(defaultValue = "0") int page
    ) {
        return service.listar(page);
    }

    // ==========================
    // OBTENER POR ID
    // ==========================
    @GetMapping("/{id}")
    public MaestroPartidaResponse obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id);
    }

    // ==========================
    // CREAR
    // ==========================
    @PostMapping
    public MaestroPartidaResponse crear(
            @RequestBody MaestroPartidaResponse request
    ) {
        return service.crear(request);
    }

    // ==========================
    // EDITAR
    // ==========================
    @PutMapping("/{id}")
    public MaestroPartidaResponse editar(
            @PathVariable Integer id,
            @RequestBody MaestroPartidaResponse request
    ) {
        return service.editar(id, request);
    }

    // ==========================
    // TOGGLE (activar / desactivar)
    // ==========================
    @PatchMapping("/{id}/estado")
    public void cambiarEstado(@PathVariable Integer id) {
        service.cambiarEstado(id);
    }
}