package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.Page.sitePage.SiteFilterDTO;
import com.backend.comfutura.dto.request.SiteRequestDTO;
import com.backend.comfutura.dto.response.SiteDTO;
import com.backend.comfutura.service.SiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    // CREAR NUEVO SITE
    @PostMapping
    public ResponseEntity<SiteDTO> crear(@Valid @RequestBody SiteRequestDTO request) {
        return ResponseEntity.ok(service.guardar(request));
    }

    // LISTAR SITES CON FILTROS COMPLETOS
    @GetMapping("/filtrar")
    public ResponseEntity<PageResponseDTO<SiteDTO>> filtrar(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) String codigoSitio,
            @RequestParam(required = false) String descripcion,
            @RequestParam(required = false) String direccion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "codigoSitio") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        // Construir filtros
        SiteFilterDTO filtros = new SiteFilterDTO();
        filtros.setSearch(search);
        filtros.setActivo(activo);
        filtros.setCodigoSitio(codigoSitio);
        filtros.setDescripcion(descripcion);
        filtros.setDireccion(direccion);

        PageResponseDTO<SiteDTO> response = service.listarConFiltros(filtros, pageable);
        return ResponseEntity.ok(response);
    }

    // LISTAR SITES (compatibilidad con endpoint anterior)
    @GetMapping
    public ResponseEntity<PageResponseDTO<SiteDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "codigoSitio") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean activos) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        SiteFilterDTO filtros = new SiteFilterDTO();
        filtros.setActivo(activos); // Usar el mismo parámetro para compatibilidad

        PageResponseDTO<SiteDTO> response = service.listarConFiltros(filtros, pageable);
        return ResponseEntity.ok(response);
    }

    // BUSCAR SITES (búsqueda general)
    @GetMapping("/buscar")
    public ResponseEntity<PageResponseDTO<SiteDTO>> buscar(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponseDTO<SiteDTO> response = service.buscar(search, pageable);
        return ResponseEntity.ok(response);
    }

    // OBTENER SITE POR ID
    @GetMapping("/{id}")
    public ResponseEntity<SiteDTO> obtenerPorId(@PathVariable Integer id) {
        SiteDTO site = service.obtenerPorId(id);
        return ResponseEntity.ok(site);
    }

    // ACTUALIZAR SITE COMPLETO
    @PutMapping("/{id}")
    public ResponseEntity<SiteDTO> actualizar(@PathVariable Integer id,
                                              @Valid @RequestBody SiteRequestDTO request) {
        SiteDTO actualizado = service.actualizar(id, request);
        return ResponseEntity.ok(actualizado);
    }

    // TOGGLE ACTIVO/INACTIVO
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Integer id) {
        service.toggle(id);
        return ResponseEntity.ok().build();
    }

    // ELIMINAR SITE (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.toggle(id);
        return ResponseEntity.ok().build();
    }

    // VERIFICAR SI CÓDIGO EXISTE
    @GetMapping("/verificar-codigo/{codigo}")
    public ResponseEntity<Boolean> verificarCodigo(@PathVariable String codigo) {
        boolean existe = service.existeCodigoSitio(codigo);
        return ResponseEntity.ok(existe);
    }
}