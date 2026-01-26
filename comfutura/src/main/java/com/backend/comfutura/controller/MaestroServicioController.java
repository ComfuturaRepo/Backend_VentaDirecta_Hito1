package com.backend.comfutura.controller;

import com.backend.comfutura.dto.response.MaestroServicioResponse;
import com.backend.comfutura.service.MaestroServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maestro-servicios")
@RequiredArgsConstructor
public class MaestroServicioController {

    private final MaestroServicioService service;

    // ==========================
    // LISTAR (paginado)
    // ==========================
    @GetMapping
    public Page<MaestroServicioResponse> listar(
            @RequestParam(defaultValue = "0") int page
    ) {
        return service.listar(page);
    }

    // ==========================
    // OBTENER POR ID
    // ==========================
    @GetMapping("/{id}")
    public MaestroServicioResponse obtenerPorId(@PathVariable Integer id) {
        return service.obtenerPorId(id);
    }

    // ==========================
    // CREAR
    // ==========================
    @PostMapping
    public MaestroServicioResponse crear(
            @RequestBody MaestroServicioResponse request
    ) {
        return service.crear(request);
    }

    // ==========================
    // EDITAR
    // ==========================
    @PutMapping("/{id}")
    public MaestroServicioResponse editar(
            @PathVariable Integer id,
            @RequestBody MaestroServicioResponse request
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