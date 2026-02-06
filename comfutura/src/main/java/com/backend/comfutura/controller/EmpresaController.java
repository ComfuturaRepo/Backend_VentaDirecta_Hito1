package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.Empresa;
import com.backend.comfutura.service.EmpresaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@RequiredArgsConstructor
public class EmpresaController {

    private final EmpresaService empresaService;

    @GetMapping
    public ResponseEntity<PageResponseDTO<Empresa>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "razonSocial") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean activos) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        PageResponseDTO<Empresa> response;
        if (activos != null && activos) {
            response = empresaService.listarActivos(pageable);
        } else {
            response = empresaService.listar(pageable);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<PageResponseDTO<Empresa>> buscar(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponseDTO<Empresa> response = empresaService.buscar(search, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/legacy")
    public ResponseEntity<List<Empresa>> listarLegacy() {
        List<Empresa> response = empresaService.listar();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<Empresa>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Empresa> response = empresaService.listarPaginado(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obtenerPorId(@PathVariable Integer id) {
        Empresa response = empresaService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Empresa> guardar(@RequestBody Empresa empresa) {
        Empresa response = empresaService.guardar(empresa);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Empresa> toggleActivo(@PathVariable Integer id) {
        Empresa response = empresaService.toggleActivo(id);
        return ResponseEntity.ok(response);
    }
}