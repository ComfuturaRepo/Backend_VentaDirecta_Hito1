package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.model.AnalistaClienteSolicitante;
import com.backend.comfutura.service.AnalistaClienteSolicitanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    // LISTAR CON PAGINACIÓN - Nuevo endpoint
    @GetMapping("/paginated")
    public ResponseEntity<PageResponseDTO<AnalistaClienteSolicitante>> listarPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "descripcion") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean activos) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        PageResponseDTO<AnalistaClienteSolicitante> response;
        if (activos != null && activos) {
            response = service.listarActivos(pageable);
        } else {
            response = service.listar(pageable);
        }

        return ResponseEntity.ok(response);
    }

    // BUSCAR CON PAGINACIÓN - Nuevo endpoint
    @GetMapping("/buscar")
    public ResponseEntity<PageResponseDTO<AnalistaClienteSolicitante>> buscar(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponseDTO<AnalistaClienteSolicitante> response = service.buscar(search, pageable);
        return ResponseEntity.ok(response);
    }

    // LISTAR COMPLETO (sin paginación) - Mantener compatibilidad
    @GetMapping
    public ResponseEntity<List<AnalistaClienteSolicitante>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // OBTENER POR ID - Nuevo endpoint
    @GetMapping("/{id}")
    public ResponseEntity<AnalistaClienteSolicitante> obtenerPorId(@PathVariable Integer id) {
        AnalistaClienteSolicitante analista = service.obtenerPorId(id);
        return ResponseEntity.ok(analista);
    }

    // TOGGLE ACTIVO/INACTIVO
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Integer id) {
        service.toggle(id);
        return ResponseEntity.ok().build();
    }

    // ACTUALIZAR - Nuevo endpoint (alternativa a POST para editar)
    @PutMapping("/{id}")
    public ResponseEntity<AnalistaClienteSolicitante> actualizar(
            @PathVariable Integer id,
            @RequestBody AnalistaClienteSolicitante analista) {
        analista.setId(id);
        AnalistaClienteSolicitante actualizado = service.guardar(analista);
        return ResponseEntity.ok(actualizado);
    }
}