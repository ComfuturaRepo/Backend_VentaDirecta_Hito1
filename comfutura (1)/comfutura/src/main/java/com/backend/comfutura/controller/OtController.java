package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.CrearOtCompletaRequest;
import com.backend.comfutura.dto.response.OtFullDetailResponse;
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

    // Listado paginado
    @GetMapping
    public ResponseEntity<Page<OtResponse>> listar(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idOts,desc") String sort) {

        String[] parts = sort.split(",");
        Sort.Direction dir = parts.length > 1 && "asc".equalsIgnoreCase(parts[1])
                ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, parts[0]));

        return ResponseEntity.ok(otService.listarOts(activo, pageable));
    }

    // Detalle completo por ID (principal para vista y edición)
    @GetMapping("/{id}")
    public ResponseEntity<OtFullDetailResponse> obtenerDetalle(@PathVariable Integer id) {
        return ResponseEntity.ok(otService.obtenerDetalleCompleto(id));
    }

    // Detalle por número OT legible (opcional, muy útil)
    @GetMapping("/numero/{numeroOt}")
    public ResponseEntity<OtFullDetailResponse> obtenerPorNumero(@PathVariable Integer numeroOt) {
        return ResponseEntity.ok(otService.obtenerPorNumeroOt(numeroOt));
    }

    // Crear o editar OT completa
    @PostMapping("/completa")
    public ResponseEntity<OtResponse> guardarCompleta(@Valid @RequestBody CrearOtCompletaRequest request) {
        OtResponse response = otService.saveOtCompleta(request);
        return ResponseEntity.ok(response);
    }

    // Toggle activo/inactivo
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleEstado(@PathVariable Integer id) {
        otService.toggleEstado(id);
        return ResponseEntity.ok().build();
    }
}