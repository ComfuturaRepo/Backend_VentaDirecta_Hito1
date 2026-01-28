package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.Site;
import com.backend.comfutura.service.SiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // LISTAR CON PAGINACIÓN - Nuevo con PageResponseDTO
    @GetMapping
    public ResponseEntity<PageResponseDTO<Site>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "codigoSitio") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean activos) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        PageResponseDTO<Site> response;
        if (activos != null && activos) {
            response = service.listarActivos(pageable);
        } else {
            response = service.listar(pageable);
        }

        return ResponseEntity.ok(response);
    }

    // BUSCAR SITES - Con búsqueda de texto
    @GetMapping("/buscar")
    public ResponseEntity<PageResponseDTO<Site>> buscar(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponseDTO<Site> response = service.buscar(search, pageable);
        return ResponseEntity.ok(response);
    }

    // MANTENER COMPATIBILIDAD - Método antiguo
    @GetMapping("/legacy")
    public ResponseEntity<Page<Site>> listarLegacy(Pageable pageable) {
        return ResponseEntity.ok(service.listarPaginado(pageable));
    }

    // TOGGLE ACTIVO/INACTIVO
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Integer id) {
        service.toggle(id);
        return ResponseEntity.ok().build();
    }

    // OBTENER SITE POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Site> obtenerPorId(@PathVariable Integer id) {
        Site site = service.obtenerPorId(id);
        return ResponseEntity.ok(site);
    }

    // ACTUALIZAR SITE
    @PutMapping("/{id}")
    public ResponseEntity<Site> actualizar(@PathVariable Integer id, @RequestBody Site site) {
        site.setIdSite(id);
        Site actualizado = service.guardar(site);
        return ResponseEntity.ok(actualizado);
    }

    // ELIMINAR SITE (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.toggle(id);
        return ResponseEntity.ok().build();
    }




}