package com.backend.comfutura.controller;

import com.backend.comfutura.model.AnalistaClienteSolicitante;
import com.backend.comfutura.service.AnalistaClienteSolicitanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analista-cliente-solicitante")
@RequiredArgsConstructor
public class AnalistaClienteSolicitanteController {

    private final AnalistaClienteSolicitanteService service;

    // CREAR + EDITAR
    @PostMapping
    public ResponseEntity<AnalistaClienteSolicitante> guardar(
            @RequestBody AnalistaClienteSolicitante analista
    ) {
        return ResponseEntity.ok(service.guardar(analista));
    }

    // LISTAR
    @GetMapping
    public ResponseEntity<List<AnalistaClienteSolicitante>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // TOGGLE
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Integer id) {
        service.toggle(id);
        return ResponseEntity.ok().build();
    }
}
