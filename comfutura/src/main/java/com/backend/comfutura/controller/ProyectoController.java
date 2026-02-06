package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.response.ProyectoResponse;
import com.backend.comfutura.model.Proyecto;
import com.backend.comfutura.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    // Listar proyectos activos con paginación
    @GetMapping
    public ResponseEntity<PageResponseDTO<ProyectoResponse>> listarProyectos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean todos) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        PageResponseDTO<ProyectoResponse> response;
        if (todos != null && todos) {
            response = proyectoService.listarTodos(pageable);
        } else {
            response = proyectoService.listarProyectos(pageable);
        }

        return ResponseEntity.ok(response);
    }

    // Buscar proyectos
    @GetMapping("/buscar")
    public ResponseEntity<PageResponseDTO<ProyectoResponse>> buscarProyectos(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponseDTO<ProyectoResponse> response = proyectoService.buscarProyectos(search, pageable);
        return ResponseEntity.ok(response);
    }

    // Para mantener compatibilidad con el endpoint existente
    @GetMapping("/legacy")
    public ResponseEntity<Page<ProyectoResponse>> listarProyectosLegacy(
            @RequestParam(defaultValue = "0") int page) {
        Page<ProyectoResponse> response = proyectoService.listarProyectos(page);
        return ResponseEntity.ok(response);
    }

    // Crear proyecto
    @PostMapping
    public ResponseEntity<ProyectoResponse> crearProyecto(@RequestBody Proyecto proyecto) {
        ProyectoResponse response = proyectoService.crearProyecto(proyecto);
        return ResponseEntity.ok(response);
    }

    // Editar proyecto
    @PutMapping("/{id}")
    public ResponseEntity<ProyectoResponse> editarProyecto(
            @PathVariable Integer id,
            @RequestBody Proyecto proyectoActualizado) {
        ProyectoResponse response = proyectoService.editarProyecto(id, proyectoActualizado);
        return ResponseEntity.ok(response);
    }

    // Obtener proyecto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponse> obtenerProyectoPorId(@PathVariable Integer id) {
        ProyectoResponse response = proyectoService.obtenerProyectoPorId(id);
        return ResponseEntity.ok(response);
    }

    // Toggle activar/desactivar proyecto
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ProyectoResponse> toggleProyecto(@PathVariable Integer id) {
        ProyectoResponse response = proyectoService.toggleProyecto(id);
        return ResponseEntity.ok(response);
    }
}