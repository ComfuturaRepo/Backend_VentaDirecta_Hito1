package com.backend.comfutura.controller;

import com.backend.comfutura.dto.response.MaestroMaterialResponse;
import com.backend.comfutura.service.MaestroMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maestro-materiales")
@RequiredArgsConstructor
public class MaestroMaterialController {

    private final MaestroMaterialService service;

    // ==========================
    // LISTAR (paginado)
    // ==========================
    @GetMapping
    public Page<MaestroMaterialResponse> listar(
            @RequestParam(defaultValue = "0") int page
    ) {
        return service.listar(page);
    }

    // ==========================
    // OBTENER POR ID
    // ==========================
    @GetMapping("/{id}")
    public MaestroMaterialResponse obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id);
    }

    // ==========================
    // CREAR
    // ==========================
    @PostMapping
    public MaestroMaterialResponse crear(
            @RequestBody MaestroMaterialResponse request
    ) {
        return service.crear(request);
    }

    // ==========================
    // EDITAR
    // ==========================
    @PutMapping("/{id}")
    public MaestroMaterialResponse editar(
            @PathVariable Integer id,
            @RequestBody MaestroMaterialResponse request
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