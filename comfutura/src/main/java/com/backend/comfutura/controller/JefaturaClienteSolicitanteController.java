package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.JefaturaClienteSolicitante;
import com.backend.comfutura.service.JefaturaClienteSolicitanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jefaturas-cliente")
@RequiredArgsConstructor
public class JefaturaClienteSolicitanteController {

    private final JefaturaClienteSolicitanteService service;

    @GetMapping
    public ResponseEntity<PageResponseDTO<JefaturaClienteSolicitante>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "descripcion") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean activos) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        PageResponseDTO<JefaturaClienteSolicitante> response;
        if (activos != null && activos) {
            response = service.listarActivos(pageable);
        } else {
            response = service.listar(pageable);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<PageResponseDTO<JefaturaClienteSolicitante>> buscar(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponseDTO<JefaturaClienteSolicitante> response = service.buscar(search, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/legacy")
    public ResponseEntity<List<JefaturaClienteSolicitante>> listarLegacy() {
        List<JefaturaClienteSolicitante> response = service.listar();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<JefaturaClienteSolicitante> guardar(@RequestBody JefaturaClienteSolicitante jefatura) {
        JefaturaClienteSolicitante response = service.guardar(jefatura);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Integer id) {
        service.toggle(id);
        return ResponseEntity.ok().build();
    }
}