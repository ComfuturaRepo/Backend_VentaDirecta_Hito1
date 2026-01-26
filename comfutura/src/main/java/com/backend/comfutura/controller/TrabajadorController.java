package com.backend.comfutura.controller;

import com.backend.comfutura.dto.*;
import com.backend.comfutura.dto.Page.MessageResponseDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.TrabajadorRequestDTO;
import com.backend.comfutura.dto.request.TrabajadorUpdateDTO;
import com.backend.comfutura.dto.response.TrabajadorDetailDTO;
import com.backend.comfutura.dto.response.TrabajadorSimpleDTO;
import com.backend.comfutura.dto.response.TrabajadorStatsDTO;
import com.backend.comfutura.service.TrabajadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trabajadores")
@RequiredArgsConstructor
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    // GET: Listar trabajadores con paginación y filtros
    @GetMapping
    public ResponseEntity<PageResponseDTO<TrabajadorSimpleDTO>> getAllTrabajadores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idTrabajador") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean activos,
            @RequestParam(required = false) Integer areaId,
            @RequestParam(required = false) Integer cargoId,
            @RequestParam(required = false) Integer empresaId,
            @RequestParam(required = false) String search) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        PageResponseDTO<TrabajadorSimpleDTO> response;

        if (search != null || areaId != null || cargoId != null || empresaId != null) {
            response = trabajadorService.searchTrabajadores(
                    search, activos, areaId, cargoId, empresaId, pageable);
        } else if (activos != null && activos) {
            response = trabajadorService.findTrabajadoresActivos(pageable);
        } else {
            response = trabajadorService.findAllTrabajadores(pageable);
        }

        return ResponseEntity.ok(response);
    }

    // GET: Obtener trabajador por ID
    @GetMapping("/{id}")
    public ResponseEntity<TrabajadorDetailDTO> getTrabajadorById(@PathVariable Integer id) {
        try {
            TrabajadorDetailDTO trabajador = trabajadorService.findTrabajadorById(id);
            return ResponseEntity.ok(trabajador);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // GET: Obtener trabajador por DNI
    @GetMapping("/buscar/dni")
    public ResponseEntity<TrabajadorDetailDTO> getTrabajadorByDni(@RequestParam String dni) {
        try {
            TrabajadorDetailDTO trabajador = trabajadorService.findTrabajadorByDni(dni);
            return ResponseEntity.ok(trabajador);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: Crear nuevo trabajador
    @PostMapping
    public ResponseEntity<?> createTrabajador(@Valid @RequestBody TrabajadorRequestDTO trabajadorDTO) {
        try {
            TrabajadorDetailDTO nuevoTrabajador = trabajadorService.createTrabajador(trabajadorDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTrabajador);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // PUT: Actualizar trabajador
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTrabajador(
            @PathVariable Integer id,
            @Valid @RequestBody TrabajadorUpdateDTO trabajadorDTO) {
        try {
            TrabajadorDetailDTO trabajadorActualizado = trabajadorService.updateTrabajador(id, trabajadorDTO);
            return ResponseEntity.ok(trabajadorActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // PATCH: Activar/Desactivar trabajador
    @PatchMapping("/{id}/toggle-activo")
    public ResponseEntity<?> toggleTrabajadorActivo(@PathVariable Integer id) {
        try {
            TrabajadorDetailDTO trabajador = trabajadorService.toggleTrabajadorActivo(id);
            return ResponseEntity.ok(trabajador);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // GET: Estadísticas de trabajadores
    @GetMapping("/estadisticas")
    public ResponseEntity<TrabajadorStatsDTO> getEstadisticas() {
        TrabajadorStatsDTO estadisticas = trabajadorService.getTrabajadorStats();
        return ResponseEntity.ok(estadisticas);
    }

    // GET: Contar trabajadores activos por área
    @GetMapping("/contar/area/{areaId}")
    public ResponseEntity<Long> countActivosByArea(@PathVariable Integer areaId) {
        long count = trabajadorService.countActivosByArea(areaId);
        return ResponseEntity.ok(count);
    }

    // GET: Contar trabajadores por cargo
    @GetMapping("/contar/cargo/{cargoId}")
    public ResponseEntity<Long> countByCargo(@PathVariable Integer cargoId) {
        long count = trabajadorService.countByCargo(cargoId);
        return ResponseEntity.ok(count);
    }

    // GET: Buscar trabajadores por nombre o apellido
    @GetMapping("/buscar/nombre")
    public ResponseEntity<PageResponseDTO<TrabajadorSimpleDTO>> buscarPorNombre(
            @RequestParam String nombre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponseDTO<TrabajadorSimpleDTO> response =
                trabajadorService.searchTrabajadores(nombre, null, null, null, null, pageable);

        return ResponseEntity.ok(response);
    }
}