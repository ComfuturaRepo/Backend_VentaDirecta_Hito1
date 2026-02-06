package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageRequestDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.areaDTO.AreaCreateUpdateDTO;
import com.backend.comfutura.dto.request.areaDTO.AreaDTO;
import com.backend.comfutura.dto.response.areaDTO.AreaSimpleDTO;
import com.backend.comfutura.service.AreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
@RequiredArgsConstructor
@Tag(name = "Áreas", description = "API para gestión de áreas")
public class AreaController {

    private final AreaService areaService;

    @Operation(summary = "Listar todas las áreas con paginación")
    @GetMapping
    public ResponseEntity<PageResponseDTO<AreaDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idArea") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PageRequestDTO pageRequest = new PageRequestDTO(page, size, sortBy, sortDir);
        PageResponseDTO<AreaDTO> response = areaService.findAll(pageRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar áreas activas con paginación")
    @GetMapping("/activas")
    public ResponseEntity<PageResponseDTO<AreaDTO>> getActivas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idArea") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PageRequestDTO pageRequest = new PageRequestDTO(page, size, sortBy, sortDir);
        PageResponseDTO<AreaDTO> response = areaService.findAllActivos(pageRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar áreas por nombre")
    @GetMapping("/search")
    public ResponseEntity<PageResponseDTO<AreaDTO>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idArea") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PageRequestDTO pageRequest = new PageRequestDTO(page, size, sortBy, sortDir);
        PageResponseDTO<AreaDTO> response = areaService.search(q, pageRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener área por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AreaDTO> getById(@PathVariable Integer id) {
        AreaDTO area = areaService.findById(id);
        return ResponseEntity.ok(area);
    }

    @Operation(summary = "Crear nueva área")
    @PostMapping
    public ResponseEntity<AreaDTO> create(@Valid @RequestBody AreaCreateUpdateDTO areaDTO) {
        AreaDTO createdArea = areaService.create(areaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdArea);
    }

    @Operation(summary = "Actualizar área existente")
    @PutMapping("/{id}")
    public ResponseEntity<AreaDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody AreaCreateUpdateDTO areaDTO) {

        AreaDTO updatedArea = areaService.update(id, areaDTO);
        return ResponseEntity.ok(updatedArea);
    }

    @Operation(summary = "Activar/desactivar área")
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<AreaDTO> toggleStatus(@PathVariable Integer id) {
        AreaDTO updatedArea = areaService.toggleStatus(id);
        return ResponseEntity.ok(updatedArea);
    }

    @Operation(summary = "Eliminar área")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        areaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar si existe un área por nombre")
    @GetMapping("/exists/nombre/{nombre}")
    public ResponseEntity<Boolean> existsByNombre(@PathVariable String nombre) {
        boolean exists = areaService.existsByNombre(nombre);
        return ResponseEntity.ok(exists);
    }

    @Operation(summary = "Obtener todas las áreas activas para dropdown")
    @GetMapping("/dropdown/activas")
    public ResponseEntity<List<AreaSimpleDTO>> getActivasForDropdown() {
        List<AreaSimpleDTO> areas = areaService.findAllActivosForDropdown();
        return ResponseEntity.ok(areas);
    }

    @Operation(summary = "Obtener todas las áreas para dropdown")
    @GetMapping("/dropdown/all")
    public ResponseEntity<List<AreaSimpleDTO>> getAllForDropdown() {
        List<AreaSimpleDTO> areas = areaService.findAllForDropdown();
        return ResponseEntity.ok(areas);
    }
}