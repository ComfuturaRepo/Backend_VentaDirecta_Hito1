package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.CrearOtCompletaRequest;
import com.backend.comfutura.dto.response.OtFullResponse;
import com.backend.comfutura.dto.response.OtResponse;
import com.backend.comfutura.service.OtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ots")
@RequiredArgsConstructor
public class OtController {

    private final OtService otService;

    // ==============================
    // CREAR o ACTUALIZAR OT COMPLETA (UPSERT)
    // ==============================
    @PostMapping("/completa")
    public ResponseEntity<OtResponse> saveOtCompleta(
            @Valid @RequestBody CrearOtCompletaRequest request
    ) {
        OtResponse response = otService.saveOtCompleta(request);

        // Si es creación nueva → 201 Created
        // Si es actualización → 200 OK
        // Como no tenemos forma directa de saber si fue create o update desde aquí,
        // usamos 200 OK para ambos (es más simple y común en APIs REST modernas)
        // Alternativa: podrías devolver 201 solo si el idOts no venía en el request
        return ResponseEntity.ok(response);
    }

    // Alternativa: si quieres diferenciar creación y actualización con endpoints separados
    // @PostMapping → create
    // @PutMapping("/{id}") → update
    // Pero como ya unificaste en saveOtCompleta, este enfoque es válido y más simple

    // ==============================
    // LISTADO DE OTs CON FILTROS
    // ==============================
    @GetMapping
    public ResponseEntity<Page<OtResponse>> listarOts(
            @RequestParam(required = false) Integer ot,           // número de OT exacto
            @RequestParam(required = false) Boolean activo,       // null = todos, true/false = filtrar
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idOts,desc") String sort  // ej: "ot,asc" o "fechaCreacion,desc"
    ) {
        // Parsear sort (campo,direccion)
        String[] sortParts = sort.split(",");
        Sort.Direction direction = sortParts.length > 1 && "asc".equalsIgnoreCase(sortParts[1])
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Sort sortBy = Sort.by(direction, sortParts[0]);

        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<OtResponse> pageResult = otService.listarOts(ot, activo, pageable);

        return ResponseEntity.ok(pageResult);
    }

    // ==============================
    // OBTENER OT BÁSICA POR ID
    // ==============================
    @GetMapping("/{id}")
    public ResponseEntity<OtResponse> obtenerPorId(@PathVariable Integer id) {
        OtResponse response = otService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    // ==============================
    // OBTENER OT COMPLETA PARA EDICIÓN
    // ==============================
    @GetMapping("/{id}/full")
    public ResponseEntity<OtFullResponse> obtenerOtParaEdicion(@PathVariable Integer id) {
        OtFullResponse response = otService.getOtForEdit(id);
        return ResponseEntity.ok(response);
    }

    // ==============================
    // TOGGLE ACTIVO / INACTIVO
    // ==============================
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleEstado(@PathVariable Integer id) {
        otService.toggleEstado(id);
        return ResponseEntity.ok().build();
    }
}