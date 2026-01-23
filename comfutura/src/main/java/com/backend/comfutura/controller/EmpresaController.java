package com.backend.comfutura.controller;


import com.backend.comfutura.model.Empresa;
import com.backend.comfutura.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService service;

    @GetMapping
    public ResponseEntity<List<Empresa>> listar() {
        return ResponseEntity.ok(service.listar());
    }



    @PostMapping
    public ResponseEntity<Empresa> crear(@RequestBody Empresa empresa) {
        return ResponseEntity.ok(service.guardar(empresa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> editar(
            @PathVariable Integer id,
            @RequestBody Empresa empresa
    ) {
        empresa.setId(id);
        return ResponseEntity.ok(service.guardar(empresa));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Empresa> toggle(@PathVariable Integer id) {
        return ResponseEntity.ok(service.toggleActivo(id));
    }
}