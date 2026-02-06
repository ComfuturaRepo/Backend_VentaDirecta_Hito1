package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.TrabajadorFilterDTO;
import com.backend.comfutura.dto.request.trabajadorDTO.TrabajadorRequestDTO;
import com.backend.comfutura.dto.request.trabajadorDTO.TrabajadorUpdateDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorDetailDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorSimpleDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorStatsDTO;
import com.backend.comfutura.service.TrabajadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trabajadores")
@RequiredArgsConstructor
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    // GET: Obtener todos los trabajadores (paginado)
    @GetMapping
    public ResponseEntity<PageResponseDTO<TrabajadorSimpleDTO>> getAllTrabajadores(
            @PageableDefault(size = 10, sort = "fechaCreacion") Pageable pageable) {
        return ResponseEntity.ok(trabajadorService.findAllTrabajadores(pageable));
    }

    // GET: Buscar trabajadores con filtros avanzados
    @PostMapping("/search")
    public ResponseEntity<PageResponseDTO<TrabajadorSimpleDTO>> searchTrabajadores(
            @RequestBody TrabajadorFilterDTO filterDTO) {
        return ResponseEntity.ok(trabajadorService.searchTrabajadoresAdvanced(filterDTO));
    }

    // GET: Buscar por texto
    @GetMapping("/search")
    public ResponseEntity<PageResponseDTO<TrabajadorSimpleDTO>> searchTrabajadores(
            @RequestParam(required = false) String search,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(trabajadorService.searchTrabajadores(search, pageable));
    }

    // GET: Obtener trabajador por ID
    @GetMapping("/{id}")
    public ResponseEntity<TrabajadorDetailDTO> getTrabajadorById(@PathVariable Integer id) {
        return ResponseEntity.ok(trabajadorService.findTrabajadorById(id));
    }

    // GET: Verificar si correo existe
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        try {
            trabajadorService.findTrabajadorByEmail(email);
            return ResponseEntity.ok(true);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(false);
        }
    }

    // GET: Verificar si DNI existe
    @GetMapping("/check-dni")
    public ResponseEntity<Boolean> checkDniExists(@RequestParam String dni) {
        try {
            trabajadorService.findTrabajadorByDni(dni);
            return ResponseEntity.ok(true);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(false);
        }
    }

    // POST: Crear nuevo trabajador
    @PostMapping
    public ResponseEntity<TrabajadorDetailDTO> createTrabajador(
            @Valid @RequestBody TrabajadorRequestDTO trabajadorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(trabajadorService.createTrabajador(trabajadorDTO));
    }

    // PUT: Actualizar trabajador
    @PutMapping("/{id}")
    public ResponseEntity<TrabajadorDetailDTO> updateTrabajador(
            @PathVariable Integer id,
            @Valid @RequestBody TrabajadorUpdateDTO trabajadorDTO) {
        return ResponseEntity.ok(trabajadorService.updateTrabajador(id, trabajadorDTO));
    }

    // PATCH: Cambiar estado activo/inactivo
    @PatchMapping("/{id}/toggle-activo")
    public ResponseEntity<TrabajadorDetailDTO> toggleActivo(@PathVariable Integer id) {
        return ResponseEntity.ok(trabajadorService.toggleTrabajadorActivo(id));
    }

    // GET: Estadísticas
    @GetMapping("/stats")
    public ResponseEntity<TrabajadorStatsDTO> getStats() {
        return ResponseEntity.ok(trabajadorService.getTrabajadorStats());
    }

    // GET: Contar activos por área
    @GetMapping("/stats/area/{areaId}/activos")
    public ResponseEntity<Long> countActivosByArea(@PathVariable Integer areaId) {
        return ResponseEntity.ok(trabajadorService.countActivosByArea(areaId));
    }

    // GET: Obtener trabajadores por filtros (para selects, combos, etc.)
    @GetMapping("/filter")
    public ResponseEntity<List<TrabajadorSimpleDTO>> getTrabajadoresByFilters(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) List<Integer> areaIds,
            @RequestParam(required = false) List<Integer> cargoIds,
            @RequestParam(required = false) List<Boolean> roles) {

        return ResponseEntity.ok(trabajadorService.getTrabajadoresByFilters(
                search, activo, areaIds, cargoIds, roles));
    }
}