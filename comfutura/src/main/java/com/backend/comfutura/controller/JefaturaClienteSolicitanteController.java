package com.backend.comfutura.controller;

import com.backend.comfutura.model.JefaturaClienteSolicitante;
import com.backend.comfutura.service.JefaturaClienteSolicitanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jefatura-cliente-solicitante")
@RequiredArgsConstructor
public class JefaturaClienteSolicitanteController {

    private final JefaturaClienteSolicitanteService service;

    // CREAR + EDITAR
    @PostMapping
    public ResponseEntity<JefaturaClienteSolicitante> guardar(
            @RequestBody JefaturaClienteSolicitante jefatura
    ) {
        return ResponseEntity.ok(service.guardar(jefatura));
    }

    // LISTAR
    @GetMapping
    public ResponseEntity<List<JefaturaClienteSolicitante>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // TOGGLE
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Integer id) {
        service.toggle(id);
        return ResponseEntity.ok().build();
    }
}