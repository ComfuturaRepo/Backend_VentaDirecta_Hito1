package com.backend.comfutura.controller;

import com.backend.comfutura.model.Site;
import com.backend.comfutura.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/site")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService service;

    // CREAR + EDITAR
    @PostMapping
    public ResponseEntity<Site> guardar(@RequestBody Site site) {
        return ResponseEntity.ok(service.guardar(site));
    }

    // LISTAR CON PAGINACIÓN
    @GetMapping
    public ResponseEntity<Page<Site>> listar(Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    // TOGGLE
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Integer id) {
        service.toggle(id);
        return ResponseEntity.ok().build();
    }
}
